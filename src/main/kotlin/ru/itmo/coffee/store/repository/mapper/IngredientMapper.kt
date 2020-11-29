package ru.itmo.coffee.store.repository.mapper

import org.springframework.jdbc.core.RowMapper
import org.springframework.stereotype.Component
import ru.itmo.coffee.store.dao.Ingredient
import java.sql.ResultSet

@Component
class IngredientMapper : RowMapper<Ingredient> {
    override fun mapRow(rs: ResultSet, rowNum: Int): Ingredient =
            Ingredient(
                    rs.getLong("id"),
                    rs.getString("название"),
                    rs.getDouble("стоимость"),
                    rs.getDouble("количество_мл"),
            )
}