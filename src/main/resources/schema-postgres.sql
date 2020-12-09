-- noinspection NonAsciiCharactersForFile

DROP TABLE IF EXISTS любимые_расписания;
DROP TABLE IF EXISTS любимые_кофе;
DROP TABLE IF EXISTS компонент_кофе;
DROP TABLE IF EXISTS ингредиент;
DROP TABLE IF EXISTS оценка_кофе;
DROP TABLE IF EXISTS оценка_расписания;
DROP TABLE IF EXISTS оценка;
DROP TABLE IF EXISTS компонент_заказа;
DROP TABLE IF EXISTS запись_расписания;
DROP TABLE IF EXISTS расписание;
DROP TABLE IF EXISTS заказ;
DROP TABLE IF EXISTS десерт;
DROP TABLE IF EXISTS кофе;
DROP TABLE IF EXISTS товар;
DROP TABLE IF EXISTS клиент;
DROP TABLE IF EXISTS роли_пользователей;
DROP TABLE IF EXISTS роль;
DROP TABLE IF EXISTS кофейня;
DROP TABLE IF EXISTS адрес;

CREATE TABLE адрес
(
    id                       SERIAL PRIMARY KEY,
    страна                   TEXT,
    субъект                  TEXT,
    муниципальный_район      TEXT,
    поселение                TEXT,
    населенный_пункт         TEXT,
    планировочная_структура  TEXT,
    улица                    TEXT,
    номер_земельного_участка TEXT,
    номер_здания             TEXT,
    номер_помещения          TEXT
);

CREATE TABLE кофейня
(
    id        SERIAL PRIMARY KEY,
    id_адреса INTEGER REFERENCES адрес (id) NOT NULL,
    телефон   TEXT                          NOT NULL
);

CREATE TABLE товар
(
    id        SERIAL PRIMARY KEY,
    название  TEXT NOT NULL,
    стоимость REAL,
    фото      BYTEA
);

CREATE TABLE десерт
(
    id_товара INTEGER PRIMARY KEY REFERENCES товар (id) ON DELETE CASCADE,
    калории   FLOAT NOT NULL,
    вес       FLOAT
);

CREATE TABLE клиент
(
    id            SERIAL PRIMARY KEY,
    имя           TEXT    NOT NULL,
    фамилия       TEXT    NOT NULL,
    пол           CHAR(1) NOT NULL CHECK (пол = 'M' OR пол = 'F'),
    дата_рождения DATE,
    id_адреса     INTEGER REFERENCES адрес (id),
    email         TEXT UNIQUE,
    телефон       TEXT    NOT NULL UNIQUE,
    пароль        TEXT    NOT NULL
);

CREATE TABLE роль
(
    id       SERIAL PRIMARY KEY,
    название TEXT NOT NULL UNIQUE
);

CREATE TABLE роли_пользователей
(
    id_клиента INTEGER NOT NULL,
    id_роли    INTEGER NOT NULL,
    PRIMARY KEY (id_клиента, id_роли)
);

CREATE TABLE кофе
(
    id_товара INTEGER PRIMARY KEY REFERENCES товар (id) ON DELETE CASCADE,
    тип       CHAR                           NOT NULL CHECK (тип IN ('u', 's') ),
    состояние TEXT                           NOT NULL,
    id_автора INTEGER REFERENCES клиент (id) NOT NULL
);

CREATE TABLE ингредиент
(
    id            SERIAL PRIMARY KEY,
    название      TEXT  NOT NULL,
    стоимость     FLOAT NOT NULL,
    количество_мл FLOAT NOT NULL
);

CREATE TABLE компонент_кофе
(
    id                 SERIAL PRIMARY KEY,
    id_кофе            INTEGER REFERENCES кофе (id_товара) ON DELETE CASCADE NOT NULL,
    id_ингредиента     INTEGER REFERENCES ингредиент (id)                    NOT NULL,
    количество         FLOAT                                                 NOT NULL CHECK (количество > 0.0),
    порядок_добавления INTEGER                                               NOT NULL CHECK (порядок_добавления >= 0)
);

CREATE TABLE заказ
(
    id                 SERIAL PRIMARY KEY,
    статус_заказа      TEXT                           NOT NULL,
    id_клиента         INTEGER REFERENCES клиент (id) NOT NULL,
    id_кофейни         INTEGER REFERENCES кофейня (id),
    скидка             FLOAT CHECK (скидка >= 0.0 AND скидка <= 100),
    стоимость          FLOAT,
    время_формирования TIMESTAMP
);

CREATE TABLE компонент_заказа
(
    id         SERIAL PRIMARY KEY,
    количество INTEGER DEFAULT 1                               NOT NULL,
    id_заказа  INTEGER REFERENCES заказ (id) ON DELETE CASCADE NOT NULL,
    id_товара  INTEGER REFERENCES товар (id)                   NOT NULL
);

CREATE TABLE расписание
(
    id         SERIAL PRIMARY KEY,
    название   VARCHAR(32),
    id_клиента INTEGER REFERENCES клиент (id) NOT NULL,
    описание   TEXT,
    состояние  TEXT                           NOT NULL
);

CREATE TABLE запись_расписания
(
    id            SERIAL PRIMARY KEY,
    название      TEXT,
    id_расписания INTEGER REFERENCES расписание (id) ON DELETE CASCADE NOT NULL,
    id_заказа     INTEGER REFERENCES заказ (id)                        NOT NULL,
    день_недели   INTEGER                                              NOT NULL,
    время         TIME                                                 NOT NULL
);

CREATE TABLE оценка
(
    id         SERIAL PRIMARY KEY,
    оценка     INTEGER CHECK (оценка >= 0 AND оценка <= 5) NOT NULL,
    отзыв      TEXT                                        NOT NULL,
    id_клиента INTEGER REFERENCES клиент (id)              NOT NULL
);

CREATE TABLE оценка_кофе
(
    id_оценки INTEGER PRIMARY KEY REFERENCES оценка (id),
    id_кофе   INTEGER REFERENCES кофе (id_товара) ON DELETE CASCADE NOT NULL
);

CREATE TABLE оценка_расписания
(
    id_оценки     INTEGER PRIMARY KEY REFERENCES оценка (id),
    id_расписания INTEGER REFERENCES расписание (id) ON DELETE CASCADE NOT NULL
);

CREATE TABLE любимые_расписания
(
    id_клиента    INTEGER REFERENCES клиент (id)                       NOT NULL,
    id_расписания INTEGER REFERENCES расписание (id) ON DELETE CASCADE NOT NULL,
    PRIMARY KEY (id_клиента, id_расписания)
);

CREATE TABLE любимые_кофе
(
    id_клиента INTEGER REFERENCES клиент (id)                        NOT NULL,
    id_кофе    INTEGER REFERENCES кофе (id_товара) ON DELETE CASCADE NOT NULL,
    PRIMARY KEY (id_клиента, id_кофе)
);

CREATE OR REPLACE FUNCTION calculate_recipe_total_volume(coffee INT) RETURNS float AS
'
    DECLARE
        sum float;
    BEGIN
        SELECT SUM(компонент_кофе.количество * ингредиент.количество_мл) as whole_volume
        FROM компонент_кофе
                 JOIN ингредиент ON компонент_кофе.id_ингредиента = ингредиент.id
        WHERE компонент_кофе.id_кофе = coffee
        INTO sum;
        RETURN sum;
    END;
' LANGUAGE plpgsql;

-- триггер чтобы нельзя если вес ингредиентов больше определённого объёма
CREATE OR REPLACE FUNCTION check_recipe() RETURNS TRIGGER AS
'
    BEGIN
        IF TG_OP = ''INSERT'' THEN
            IF (calculate_recipe_total_volume(NEW.id_кофе) +
                NEW.количество * (SELECT количество_мл
                                  FROM ингредиент
                                  WHERE id = NEW.id_ингредиента)) > 400 THEN
                RETURN NULL;
            ELSE
                RETURN NEW;
            END IF;
        ELSIF TG_OP = ''UPDATE'' THEN
            IF (calculate_recipe_total_volume(NEW.id_кофе) +
                NEW.количество * (SELECT количество_мл
                                  FROM ингредиент
                                  WHERE id = NEW.id_ингредиента) -
                OLD.количество * (SELECT количество_мл
                                  FROM ингредиент
                                  WHERE id = OLD.id_ингредиента)) > 400 THEN
                RETURN NULL;
            ELSE
                RETURN NEW;
            END IF;
        END IF;
    END;
' LANGUAGE plpgsql;
CREATE TRIGGER recipe_ingredients
    BEFORE INSERT OR UPDATE
    ON компонент_кофе
    FOR EACH ROW
EXECUTE PROCEDURE check_recipe();


CREATE OR REPLACE FUNCTION delete_score() RETURNS TRIGGER AS
'
    BEGIN
        DELETE
        FROM оценка
        WHERE оценка.id = OLD.id_оценки;
        RETURN NULL;
    END;
' LANGUAGE plpgsql;
-- триггер удаления оценок при удалении оценки кофе
CREATE TRIGGER score_coffee_delete
    AFTER DELETE
    ON оценка_кофе
    FOR EACH ROW
EXECUTE PROCEDURE delete_score();

-- триггер удаления оценок при удалении оценки расписания
CREATE TRIGGER score_schedule_delete
    AFTER DELETE
    ON оценка_расписания
    FOR EACH ROW
EXECUTE PROCEDURE delete_score();

CREATE OR REPLACE FUNCTION delete_product() RETURNS TRIGGER AS
'
    BEGIN
        DELETE
        FROM товар
        WHERE товар.id = OLD.id_товара;
        RETURN NULL;
    END;
' LANGUAGE plpgsql;

-- триггер удаления товара при удалении десерта
CREATE TRIGGER desert_product_delete
    AFTER DELETE
    ON десерт
    FOR EACH ROW
EXECUTE PROCEDURE delete_product();

-- триггер удаления товара при удалении кофе
CREATE TRIGGER coffee_product_delete
    AFTER DELETE
    ON кофе
    FOR EACH ROW
EXECUTE PROCEDURE delete_product();

CREATE OR REPLACE FUNCTION createCoffee(coffee_name text, cost float, photo bytea, coffee_type char, author int,
                                        coffee_state text) RETURNS INT
    STRICT AS
'
    DECLARE
        ret int;
    BEGIN
    INSERT INTO товар(название, стоимость, фото)
    VALUES (coffee_name, cost, photo)
    RETURNING id
    INTO ret;
    INSERT INTO кофе(id_товара, тип, id_автора, состояние)
    VALUES (ret, coffee_type, author, coffee_state); RETURN ret; END;
' LANGUAGE 'plpgsql';

CREATE OR REPLACE FUNCTION createDessert(name text, price float, photo bytea, calories float, weight float) RETURNS INT
    STRICT AS
'
    DECLARE
        ret int;
    BEGIN
    INSERT INTO товар(название, стоимость, фото)
    VALUES (name, price, photo)
    RETURNING id
    INTO ret;
    INSERT INTO десерт(id_товара, калории, вес)
    VALUES (ret, calories, weight); RETURN ret; END;
' LANGUAGE 'plpgsql';

CREATE OR REPLACE FUNCTION addCoffeeRating(rating int, comments text, coffee int, client int) RETURNS INT
    STRICT AS
'
    DECLARE
        ret int;
    BEGIN
    INSERT INTO оценка(оценка, отзыв, id_клиента)
    VALUES (rating, comments, client)
    RETURNING id
    INTO ret;
    INSERT INTO оценка_кофе(id_оценки, id_кофе)
    VALUES (ret, coffee); RETURN ret; END;
' LANGUAGE 'plpgsql';

CREATE OR REPLACE FUNCTION addScheduleRating(rating int, comments text, schedule int, client int) RETURNS INT
    STRICT AS
'
    DECLARE
        ret int;
    BEGIN
    INSERT INTO оценка(оценка, отзыв, id_клиента)
    VALUES (rating, comments, client)
    RETURNING id
    INTO ret;
    INSERT INTO оценка_расписания(id_оценки, id_расписания)
    VALUES (ret, schedule); RETURN ret; END;
' LANGUAGE 'plpgsql';

CREATE OR REPLACE FUNCTION copySchedule(schedule int, client int) RETURNS INT
    STRICT AS
'
    DECLARE
        newSchedId int;
    BEGIN
    INSERT INTO расписание(название, id_клиента, описание, состояние)
    SELECT название,
           client,
           описание,
           ''HIDDEN''
    FROM расписание
    WHERE id = schedule
    RETURNING id
    INTO newSchedId;
    INSERT INTO запись_расписания(название, id_расписания, id_заказа, день_недели, время)
    SELECT название,
           newSchedId,
           id_заказа,
           день_недели,
           время
    FROM запись_расписания
    WHERE id_расписания = schedule; RETURN newSchedId; END;
' LANGUAGE 'plpgsql';

CREATE OR REPLACE FUNCTION copyCoffee(coffee int, author int) RETURNS INT
    STRICT AS
'
    DECLARE
        newCoffeeId int;
    BEGIN
    INSERT INTO товар(название, стоимость, фото)
    SELECT название, стоимость, фото
    FROM товар
    WHERE id = coffee
    RETURNING id
    INTO newCoffeeId;
    INSERT
    INTO кофе(id_товара, тип, id_автора, состояние)
    VALUES (newCoffeeId, ''u'', author, ''HIDDEN'');
    INSERT INTO компонент_кофе(id_кофе, id_ингредиента, количество, порядок_добавления)
    SELECT newCoffeeId,
           id_ингредиента,
           количество,
           порядок_добавления
    FROM компонент_кофе
    WHERE id_кофе = coffee; RETURN newCoffeeId; END;
' LANGUAGE 'plpgsql';