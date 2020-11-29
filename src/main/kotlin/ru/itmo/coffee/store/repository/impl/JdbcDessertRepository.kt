package ru.itmo.coffee.store.repository.impl

import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource
import org.springframework.jdbc.core.namedparam.SqlParameterSource
import org.springframework.jdbc.core.simple.SimpleJdbcInsert
import org.springframework.jdbc.support.GeneratedKeyHolder
import org.springframework.jdbc.support.KeyHolder
import ru.itmo.coffee.store.dao.Address
import ru.itmo.coffee.store.dao.Dessert
import ru.itmo.coffee.store.repository.DessertRepository
import ru.itmo.coffee.store.repository.mapper.DessertMapper
import java.sql.Connection
import java.sql.PreparedStatement
import javax.annotation.PostConstruct
import javax.sql.DataSource

class JdbcDessertRepository(private val jdbcTemplate: JdbcTemplate,
                            private val rowMapper: DessertMapper,
                            private val dataSource: DataSource) : DessertRepository {

    private lateinit var jdbcInsertProduct: SimpleJdbcInsert
    private lateinit var jdbcInsertDesert: SimpleJdbcInsert
    private val cache: HashMap<Long, Address> = HashMap()

    @PostConstruct
    fun init() {
        jdbcInsertProduct = SimpleJdbcInsert(dataSource)
                .withTableName("товар").usingGeneratedKeyColumns("id")
        jdbcInsertDesert = SimpleJdbcInsert(dataSource)
                .withTableName("десерт").usingGeneratedKeyColumns("id")
    }

    override fun save(dessert: Dessert): Long {
        val productParameters = HashMap<String, Any?>()
        val dessertParameters = HashMap<String, Any?>()
        productParameters["название"] = dessert.name
        productParameters["стоимость"] = dessert.cost
        productParameters["фото"] = dessert.photo
        dessert.id = jdbcInsertProduct.executeAndReturnKey(productParameters) as Long
        dessertParameters["id"] = dessert.id
        dessertParameters["id_товара"] = dessert.id
        dessertParameters["калории"] = dessert.calories
        dessertParameters["вес"] = dessert.weight
        jdbcInsertDesert.execute(dessertParameters)
        return dessert.id
    }

    override fun update(dessert: Dessert): Int {
        var updated = jdbcTemplate.update(
                "update товар set название = ?, стоимость = ?, фото = ? where id = ?",
                dessert.name, dessert.cost, dessert.photo, dessert.id)
        updated += jdbcTemplate.update(
                "update десерт set калории = ?, вес = ? where id = ?",
                dessert.calories, dessert.weight, dessert.id)
        return updated
    }

    override fun deleteById(id: Long): Int {
        return jdbcTemplate.update(
                "delete from десерт where id = ?", id)
    }

    override fun findAll(): List<Dessert> {
        return jdbcTemplate.query(
                "select * from десерт", rowMapper)
    }

    override fun findById(id: Long): Dessert? {
        return jdbcTemplate.queryForObject(
                "select * from десерт where id = ?", rowMapper)
    }
}