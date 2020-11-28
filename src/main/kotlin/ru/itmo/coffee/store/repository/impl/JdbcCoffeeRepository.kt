package ru.itmo.coffee.store.repository.impl

import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.support.GeneratedKeyHolder
import org.springframework.jdbc.support.KeyHolder
import ru.itmo.coffee.store.dao.Coffee
import ru.itmo.coffee.store.repository.CoffeeRepository
import ru.itmo.coffee.store.repository.mapper.CoffeeMapper
import java.sql.Connection
import java.sql.PreparedStatement


class JdbcCoffeeRepository(private val jdbcTemplate: JdbcTemplate, private val rowMapper: CoffeeMapper) : CoffeeRepository {
    override fun save(coffee: Coffee): Int {
        val keyHolder: KeyHolder = GeneratedKeyHolder()
        val psc: (Connection) -> PreparedStatement = { connection: Connection ->
            val ps = connection
                    .prepareStatement("insert into товар (название, стоимость, фото) values (?,?,?)")
            ps.setString(1, coffee.name)
            ps.setDouble(2, coffee.cost)
            ps.setBytes(3, coffee.photo)
            ps
        }
        jdbcTemplate.update(psc, keyHolder)
        return jdbcTemplate.update("insert into кофе (id, id_товара, тип, состояние, id_автора) values (?,?,?,?,?)",
                keyHolder.key, keyHolder.key, coffee.type, coffee.state, coffee.author)
    }

    override fun update(coffee: Coffee): Int {
        var updated = jdbcTemplate.update(
                "update товар set название = ?, стоимость = ?, фото = ? where id = ?",
                coffee.name, coffee.cost, coffee.photo, coffee.id)
        updated += jdbcTemplate.update(
                "update кофе set тип = ?, состояние = ?, id_автора = ? where id = ?",
                coffee.type, coffee.state, coffee.author?.id, coffee.id)
        return updated
    }

    override fun deleteById(id: Long): Int {
        return jdbcTemplate.update(
                "delete from кофе where id = ?", id)
    }

    override fun findAll(): List<Coffee> {
        return jdbcTemplate.query(
                "select * from кофе", rowMapper)
    }

    override fun findById(id: Long): Coffee? {
        return jdbcTemplate.queryForObject(
                "select * from кофе where id = ?", rowMapper)
    }
}