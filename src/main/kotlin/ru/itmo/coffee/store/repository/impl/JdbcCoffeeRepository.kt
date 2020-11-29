package ru.itmo.coffee.store.repository.impl

import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.simple.SimpleJdbcInsert
import org.springframework.stereotype.Repository
import ru.itmo.coffee.store.dao.Address
import ru.itmo.coffee.store.dao.Coffee
import ru.itmo.coffee.store.repository.CoffeeRepository
import ru.itmo.coffee.store.repository.mapper.CoffeeMapper
import javax.annotation.PostConstruct
import javax.sql.DataSource


@Repository
class JdbcCoffeeRepository(
        private val jdbcTemplate: JdbcTemplate,
        private val rowMapper: CoffeeMapper,
        private val dataSource: DataSource
) : CoffeeRepository {

    private lateinit var jdbcInsertProduct: SimpleJdbcInsert
    private lateinit var jdbcInsertCoffee: SimpleJdbcInsert
    private val cache: HashMap<Long, Address> = HashMap()

    @PostConstruct
    fun init() {
        jdbcInsertProduct = SimpleJdbcInsert(dataSource)
                .withTableName("товар").usingGeneratedKeyColumns("id")
        jdbcInsertCoffee = SimpleJdbcInsert(dataSource)
                .withTableName("кофе").usingGeneratedKeyColumns("id")
    }

    override fun save(coffee: Coffee): Long {
        val productParameters = HashMap<String, Any?>()
        val coffeeParameters = HashMap<String, Any?>()
        productParameters["название"] = coffee.name
        productParameters["стоимость"] = coffee.cost
        productParameters["фото"] = coffee.photo
        coffee.id = jdbcInsertProduct.executeAndReturnKey(productParameters) as Long
        coffeeParameters["id"] = coffee.id
        coffeeParameters["id_товара"] = coffee.id
        coffeeParameters["тип"] = coffee.type
        coffeeParameters["состояние"] = coffee.state
        coffeeParameters["id_автора"] = coffee.author?.id
        jdbcInsertCoffee.execute(coffeeParameters)
        return coffee.id
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