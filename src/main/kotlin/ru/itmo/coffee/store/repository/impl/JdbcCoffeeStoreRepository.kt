package ru.itmo.coffee.store.repository.impl

import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource
import org.springframework.jdbc.core.namedparam.SqlParameterSource
import org.springframework.jdbc.core.simple.SimpleJdbcInsert
import ru.itmo.coffee.store.dao.Address
import ru.itmo.coffee.store.dao.CoffeeStore
import ru.itmo.coffee.store.repository.CoffeeStoreRepository
import ru.itmo.coffee.store.repository.mapper.CoffeeStoreMapper
import javax.annotation.PostConstruct
import javax.sql.DataSource

class JdbcCoffeeStoreRepository(private val jdbcTemplate: JdbcTemplate,
                                private val rowMapper: CoffeeStoreMapper,
                                private val dataSource: DataSource) : CoffeeStoreRepository {

    private lateinit var jdbcInsert: SimpleJdbcInsert
    private val cache: HashMap<Long, Address> = HashMap()

    @PostConstruct
    fun init() {
        jdbcInsert = SimpleJdbcInsert(dataSource)
                .withTableName("кофейня").usingGeneratedKeyColumns("id");
    }

    override fun save(coffeeStore: CoffeeStore): Long {
        val parameters: SqlParameterSource = BeanPropertySqlParameterSource(coffeeStore)
        coffeeStore.id = jdbcInsert.executeAndReturnKey(parameters) as Long
        return coffeeStore.id
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