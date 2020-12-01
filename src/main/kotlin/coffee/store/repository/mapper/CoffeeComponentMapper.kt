package coffee.store.repository.mapper

import org.springframework.jdbc.core.RowMapper
import org.springframework.stereotype.Component
import coffee.store.dao.CoffeeComponent
import coffee.store.repository.IngredientRepository
import java.sql.ResultSet

@Component
class CoffeeComponentMapper(
        private val ingredientRepository: IngredientRepository,
) : RowMapper<CoffeeComponent> {
    override fun mapRow(rs: ResultSet, rowNum: Int): CoffeeComponent =
            CoffeeComponent(
                    rs.getLong("id"),
                    rs.getLong("id_кофе"),
                    ingredientRepository.findById(rs.getLong("id_ингредиента"))!!,
                    rs.getInt("количество"),
                    rs.getInt("порядок_добавления"),
            )
}