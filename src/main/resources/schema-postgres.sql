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
    id     SERIAL PRIMARY KEY,
    оценка INTEGER CHECK (оценка >= 0 AND оценка <= 5) NOT NULL,
    отзыв  TEXT                                        NOT NULL
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
