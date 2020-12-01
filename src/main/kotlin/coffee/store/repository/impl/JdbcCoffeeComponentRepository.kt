package coffee.store.repository.impl

import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.simple.SimpleJdbcInsert
import org.springframework.stereotype.Repository
import coffee.store.dao.CoffeeComponent
import coffee.store.repository.CoffeeComponentRepository
import coffee.store.repository.mapper.CoffeeComponentMapper
import javax.annotation.PostConstruct
import javax.sql.DataSource

@Repository
class JdbcCoffeeComponentRepository(
        private val dataSource: DataSource,
        private val jdbcTemplate: JdbcTemplate,
        private val rowMapper: CoffeeComponentMapper,
) : CoffeeComponentRepository {
    private lateinit var jdbcInsert: SimpleJdbcInsert

    @PostConstruct
    fun init() {
        jdbcInsert = SimpleJdbcInsert(dataSource)
                .withTableName("компонент_кофе").usingGeneratedKeyColumns("id")
    }

    override fun save(coffeeComponent: CoffeeComponent): Long {
        val parameters = HashMap<String, Any?>()
        parameters["id_кофе"] = coffeeComponent.coffeeId
        parameters["id_ингредиента"] = coffeeComponent.ingredient.id
        parameters["количество"] = coffeeComponent.quantity
        parameters["порядок_добавления"] = coffeeComponent.addingOrder
        coffeeComponent.id = jdbcInsert.executeAndReturnKey(parameters).toLong()
        return coffeeComponent.id
    }

    override fun update(coffeeComponent: CoffeeComponent): Int {
        return jdbcTemplate.update(
                "update компонент_кофе set id_кофе = ?, id_ингредиента = ?, количество = ?, порядок_добавления = ? where id = ?",
                coffeeComponent.coffeeId, coffeeComponent.ingredient.id, coffeeComponent.quantity, coffeeComponent.addingOrder,
                coffeeComponent.id)
    }

    override fun deleteById(id: Long): Int {
        return jdbcTemplate.update(
                "delete from компонент_кофе where id = ?", id)
    }

    override fun findAll(): List<CoffeeComponent> {
        return jdbcTemplate.query(
                "select * from компонент_кофе", rowMapper)
    }

    override fun findAllByCoffeeId(id: Long): MutableList<CoffeeComponent> {
        return jdbcTemplate.query(
                "select * from компонент_кофе where id_кофе = ?", rowMapper, id)
    }

    override fun findById(id: Long): CoffeeComponent? {
        return jdbcTemplate.queryForObject(
                "select * from компонент_кофе where id = ?", rowMapper, id)
    }
}