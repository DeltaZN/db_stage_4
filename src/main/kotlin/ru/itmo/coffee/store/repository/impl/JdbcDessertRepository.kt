package ru.itmo.coffee.store.repository.impl

import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.support.GeneratedKeyHolder
import org.springframework.jdbc.support.KeyHolder
import ru.itmo.coffee.store.model.Coffee
import ru.itmo.coffee.store.model.Dessert
import ru.itmo.coffee.store.repository.DessertRepository
import ru.itmo.coffee.store.repository.mapper.DessertMapper
import java.sql.Connection
import java.sql.PreparedStatement

class JdbcDessertRepository(private val jdbcTemplate: JdbcTemplate, private val rowMapper: DessertMapper) : DessertRepository {
    override fun save(dessert: Dessert): Int {
        val keyHolder: KeyHolder = GeneratedKeyHolder()
        val psc: (Connection) -> PreparedStatement = { connection: Connection ->
            val ps = connection
                    .prepareStatement("insert into товар (название, стоимость, фото) values (?,?,?)")
            ps.setString(1, dessert.name)
            ps.setDouble(2, dessert.cost)
            ps.setBytes(3, dessert.photo)
            ps
        }
        jdbcTemplate.update(psc, keyHolder)
        return jdbcTemplate.update("insert into десерт (id, id_товара, калории, вес) values (?,?,?,?)",
                keyHolder.key, keyHolder.key, dessert.calories, dessert.weight)
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