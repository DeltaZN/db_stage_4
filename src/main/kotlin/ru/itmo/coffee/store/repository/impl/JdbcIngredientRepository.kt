package ru.itmo.coffee.store.repository.impl

import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.simple.SimpleJdbcInsert
import org.springframework.stereotype.Repository
import ru.itmo.coffee.store.dao.Ingredient
import ru.itmo.coffee.store.repository.IngredientRepository
import ru.itmo.coffee.store.repository.mapper.IngredientMapper
import javax.annotation.PostConstruct
import javax.sql.DataSource

@Repository
class JdbcIngredientRepository(
        private val dataSource: DataSource,
        private val jdbcTemplate: JdbcTemplate,
        private val rowMapper: IngredientMapper,
) : IngredientRepository {
    private lateinit var jdbcInsert: SimpleJdbcInsert
    private val cache: HashMap<Long, Ingredient> = HashMap()

    @PostConstruct
    fun init() {
        jdbcInsert = SimpleJdbcInsert(dataSource)
                .withTableName("ингредиент").usingGeneratedKeyColumns("id")
    }

    override fun save(ingredient: Ingredient): Long {
        val parameters = HashMap<String, Any?>()
        parameters["название"] = ingredient.name
        parameters["стоимость"] = ingredient.cost
        parameters["количество_мл"] = ingredient.volumeMl
        ingredient.id = jdbcInsert.executeAndReturnKey(parameters).toLong()
        cache[ingredient.id] = ingredient
        return ingredient.id
    }

    override fun update(ingredient: Ingredient): Int {
        cache[ingredient.id] = ingredient
        return jdbcTemplate.update(
                "update ингредиент set название = ?, стоимость = ?, количество_мл = ? where id = ?",
                ingredient.name, ingredient.cost, ingredient.volumeMl, ingredient.id)
    }

    override fun deleteById(id: Long): Int {
        cache.remove(id)
        return jdbcTemplate.update(
                "delete from ингредиент where id = ?", id)
    }

    override fun findAll(): List<Ingredient> {
        return jdbcTemplate.query(
                "select * from ингредиент", rowMapper)
    }

    override fun findById(id: Long): Ingredient? {
        return if (cache[id] != null) {
            cache[id]
        } else {
            val res = jdbcTemplate.queryForObject(
                    "select * from ингредиент where id = ?", rowMapper)
            if (res != null) {
                cache[res.id] = res
            }
            res
        }
    }
}