package coffee.store.repository.impl

import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.simple.SimpleJdbcInsert
import org.springframework.stereotype.Repository
import coffee.store.dao.Address
import coffee.store.dao.Dessert
import coffee.store.repository.DessertRepository
import coffee.store.repository.mapper.DessertMapper
import javax.annotation.PostConstruct
import javax.sql.DataSource

@Repository
class JdbcDessertRepository(
        private val jdbcTemplate: JdbcTemplate,
        private val rowMapper: DessertMapper,
        private val dataSource: DataSource
) : DessertRepository {

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
                "select * from десерт join товар on десерт.id_товара = товар.id", rowMapper)
    }

    override fun findById(id: Long): Dessert? {
        return jdbcTemplate.queryForObject(
                "select * from десерт join товар on десерт.id_товара = товар.id where десерт.id = ?", rowMapper, id)
    }
}