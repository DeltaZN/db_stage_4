package ru.itmo.coffee.store

import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.jdbc.core.JdbcTemplate
import ru.itmo.coffee.store.model.Customer
import ru.itmo.coffee.store.model.Sex
import ru.itmo.coffee.store.repository.CustomerRepository

fun main(args: Array<String>) {
	runApplication<Application>(*args)
}

@SpringBootApplication
class Application(private val jdbcTemplate: JdbcTemplate, private val customerRepository: CustomerRepository) : CommandLineRunner {

	override fun run(vararg args: String?) {
		dropTables()
		createTables()
		val customer = Customer(0, "Georgii", "Savin", Sex.M, null, null, null, "123123")
		customerRepository.save(customer)
	}

	fun dropTables() {
		jdbcTemplate.execute("DROP TABLE любимые_расписания;\n" +
				"DROP TABLE любимые_кофе;\n" +
				"DROP TABLE компонент_кофе;\n" +
				"DROP TABLE ингредиент;\n" +
				"DROP TABLE оценка_кофе;\n" +
				"DROP TABLE оценка_расписания;\n" +
				"DROP TABLE оценка;\n" +
				"DROP TABLE компонент_заказа;\n" +
				"DROP TABLE запись_расписания;\n" +
				"DROP TABLE расписание;\n" +
				"DROP TABLE заказ;\n" +
				"DROP TABLE десерт;\n" +
				"DROP TABLE кофе;\n" +
				"DROP TABLE товар;\n" +
				"DROP TABLE клиент;\n" +
				"DROP TABLE кофейня;\n" +
				"DROP TABLE адрес;")
	}

	fun createTables() {
		jdbcTemplate.execute("CREATE TABLE адрес\n" +
				"(\n" +
				"    id                       SERIAL PRIMARY KEY,\n" +
				"    страна                   TEXT,\n" +
				"    субъект                  TEXT,\n" +
				"    муниципальный_район      TEXT,\n" +
				"    поселение                TEXT,\n" +
				"    населенный_пункт         TEXT,\n" +
				"    планировочная_структура  TEXT,\n" +
				"    улица                    TEXT,\n" +
				"    номер_земельного_участка TEXT,\n" +
				"    номер_здания             TEXT,\n" +
				"    номер_помещения          TEXT\n" +
				");\n" +
				"\n" +
				"CREATE TABLE кофейня\n" +
				"(\n" +
				"    id        SERIAL PRIMARY KEY,\n" +
				"    id_адреса INTEGER REFERENCES адрес (id) NOT NULL,\n" +
				"    телефон   TEXT                          NOT NULL\n" +
				");\n" +
				"\n" +
				"CREATE TABLE товар\n" +
				"(\n" +
				"    id        SERIAL PRIMARY KEY,\n" +
				"    название  TEXT NOT NULL,\n" +
				"    стоимость REAL,\n" +
				"    фото      BYTEA\n" +
				");\n" +
				"\n" +
				"CREATE TABLE десерт\n" +
				"(\n" +
				"    id        INTEGER PRIMARY KEY,\n" +
				"    id_товара INTEGER REFERENCES товар (id) ON DELETE CASCADE NOT NULL UNIQUE,\n" +
				"    калории   FLOAT                                           NOT NULL,\n" +
				"    вес       FLOAT,\n" +
				"    CONSTRAINT товар CHECK (id = id_товара)\n" +
				");\n" +
				"\n" +
				"CREATE TABLE клиент\n" +
				"(\n" +
				"    id            SERIAL PRIMARY KEY,\n" +
				"    имя           TEXT    NOT NULL,\n" +
				"    фамилия       TEXT    NOT NULL,\n" +
				"    пол           CHAR(1) NOT NULL CHECK (пол = 'М' OR пол = 'Ж'),\n" +
				"    дата_рождения DATE,\n" +
				"    id_адреса     INTEGER REFERENCES адрес (id),\n" +
				"    email         TEXT UNIQUE,\n" +
				"    телефон       TEXT    NOT NULL UNIQUE\n" +
				");\n" +
				"\n" +
				"CREATE TABLE кофе\n" +
				"(\n" +
				"    id        INTEGER PRIMARY KEY,\n" +
				"    id_товара INTEGER REFERENCES товар (id) ON DELETE CASCADE NOT NULL UNIQUE,\n" +
				"    тип       CHAR                                            NOT NULL CHECK (тип IN ('u', 's') ),\n" +
				"    состояние TEXT                                            NOT NULL,\n" +
				"    id_автора INTEGER REFERENCES клиент (id)                  NOT NULL\n" +
				"        CONSTRAINT товар CHECK (id = id_товара)\n" +
				");\n" +
				"\n" +
				"CREATE TABLE ингредиент\n" +
				"(\n" +
				"    id            SERIAL PRIMARY KEY,\n" +
				"    название      TEXT  NOT NULL,\n" +
				"    стоимость     FLOAT NOT NULL,\n" +
				"    количество_мл FLOAT NOT NULL\n" +
				");\n" +
				"\n" +
				"CREATE TABLE компонент_кофе\n" +
				"(\n" +
				"    id                 SERIAL PRIMARY KEY,\n" +
				"    id_кофе            INTEGER REFERENCES кофе (id) ON DELETE CASCADE NOT NULL,\n" +
				"    id_ингредиента     INTEGER REFERENCES ингредиент (id)             NOT NULL,\n" +
				"    количество         INTEGER                                        NOT NULL CHECK (количество > 0),\n" +
				"    порядок_добавления INTEGER                                        NOT NULL CHECK (порядок_добавления >= 0)\n" +
				");\n" +
				"\n" +
				"CREATE TABLE заказ\n" +
				"(\n" +
				"    id                 SERIAL PRIMARY KEY,\n" +
				"    статус_заказа      TEXT                            NOT NULL,\n" +
				"    id_клиента         INTEGER REFERENCES клиент (id)  NOT NULL,\n" +
				"    id_кофейни         INTEGER REFERENCES кофейня (id) NOT NULL,\n" +
				"    скидка             FLOAT CHECK (скидка >= 0.0 AND скидка <= 100),\n" +
				"    стоимость          FLOAT,\n" +
				"    время_формирования TIMESTAMP\n" +
				");\n" +
				"\n" +
				"CREATE TABLE компонент_заказа\n" +
				"(\n" +
				"    id        SERIAL PRIMARY KEY,\n" +
				"    id_заказа INTEGER REFERENCES заказ (id) ON DELETE CASCADE NOT NULL,\n" +
				"    id_товара INTEGER REFERENCES товар (id)                   NOT NULL\n" +
				");\n" +
				"\n" +
				"CREATE TABLE расписание\n" +
				"(\n" +
				"    id         SERIAL PRIMARY KEY,\n" +
				"    название   VARCHAR(32),\n" +
				"    id_клиента INTEGER REFERENCES клиент (id) NOT NULL,\n" +
				"    описание   TEXT,\n" +
				"    состояние  TEXT                           NOT NULL\n" +
				");\n" +
				"\n" +
				"CREATE TABLE запись_расписания\n" +
				"(\n" +
				"    id            SERIAL PRIMARY KEY,\n" +
				"    название      TEXT,\n" +
				"    id_расписания INTEGER REFERENCES расписание (id) ON DELETE CASCADE NOT NULL,\n" +
				"    id_заказа     INTEGER REFERENCES заказ (id)                        NOT NULL,\n" +
				"    день_недели   INTEGER                                              NOT NULL,\n" +
				"    время         TIME                                                 NOT NULL\n" +
				");\n" +
				"\n" +
				"CREATE TABLE оценка\n" +
				"(\n" +
				"    id     SERIAL PRIMARY KEY,\n" +
				"    оценка INTEGER CHECK (оценка >= 0 AND оценка <= 5) NOT NULL,\n" +
				"    отзыв  TEXT                                        NOT NULL\n" +
				");\n" +
				"\n" +
				"CREATE TABLE оценка_кофе\n" +
				"(\n" +
				"    id        INTEGER PRIMARY KEY,\n" +
				"    id_оценки INTEGER REFERENCES оценка (id)                 NOT NULL UNIQUE,\n" +
				"    id_кофе   INTEGER REFERENCES кофе (id) ON DELETE CASCADE NOT NULL,\n" +
				"    CONSTRAINT оценка CHECK (id = id_оценки)\n" +
				");\n" +
				"\n" +
				"CREATE TABLE оценка_расписания\n" +
				"(\n" +
				"    id            INTEGER PRIMARY KEY,\n" +
				"    id_оценки     INTEGER REFERENCES оценка (id)                       NOT NULL UNIQUE,\n" +
				"    id_расписания INTEGER REFERENCES расписание (id) ON DELETE CASCADE NOT NULL,\n" +
				"    CONSTRAINT оценка CHECK (id = id_оценки)\n" +
				");\n" +
				"\n" +
				"CREATE TABLE любимые_расписания\n" +
				"(\n" +
				"    id            SERIAL PRIMARY KEY,\n" +
				"    id_клиента    INTEGER REFERENCES клиент (id)                       NOT NULL,\n" +
				"    id_расписания INTEGER REFERENCES расписание (id) ON DELETE CASCADE NOT NULL\n" +
				");\n" +
				"\n" +
				"CREATE TABLE любимые_кофе\n" +
				"(\n" +
				"    id         SERIAL PRIMARY KEY,\n" +
				"    id_клиента INTEGER REFERENCES клиент (id)                 NOT NULL,\n" +
				"    id_кофе    INTEGER REFERENCES кофе (id) ON DELETE CASCADE NOT NULL\n" +
				");")
	}

}
