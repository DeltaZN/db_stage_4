package ru.itmo.coffee.store.repository.impl

import org.springframework.jdbc.core.JdbcTemplate
import ru.itmo.coffee.store.model.CoffeeScore
import ru.itmo.coffee.store.model.CoffeeStore
import ru.itmo.coffee.store.model.Customer
import ru.itmo.coffee.store.repository.CoffeeStoreRepository
import ru.itmo.coffee.store.repository.mapper.CoffeeStoreMapper

class JdbcCoffeeStoreRepository(private val jdbcTemplate: JdbcTemplate, private val rowMapper: CoffeeStoreMapper) : CoffeeStoreRepository {
    override fun save(coffeeStore: CoffeeStore): Int {
        return jdbcTemplate.update(
                "insert into кофейня (id_адреса, телефон) values (?,?)",
                coffeeStore.address?.id, coffeeStore.phone)
    }

    override fun update(coffeeStore: CoffeeStore): Int {
        return jdbcTemplate.update(
                "update кофейня set id_адреса = ?, телефон = ? where id = ?",
                coffeeStore.address?.id, coffeeStore.phone, coffeeStore.id)
    }

    override fun deleteById(id: Long): Int {
        return jdbcTemplate.update(
                "delete from кофейня where id = ?", id)
    }

    override fun findAll(): List<CoffeeStore> {
        return jdbcTemplate.query("select * from кофейня", rowMapper)
    }

    override fun findById(id: Long): CoffeeStore? {
        return jdbcTemplate.queryForObject(
                "select * from кофейня where id = ?", rowMapper)
    }
}