package coffee.store.repository.mapper

import org.springframework.jdbc.core.RowMapper
import org.springframework.stereotype.Component
import coffee.store.dao.Coffee
import coffee.store.repository.CoffeeComponentRepository
import coffee.store.repository.UserRepository
import coffee.store.repository.jpa.UserJpaRepository
import java.sql.ResultSet

@Component
class CoffeeMapper(
        private val userRepository: UserJpaRepository,
        private val coffeeComponentRepository: CoffeeComponentRepository,
) : RowMapper<Coffee> {
    override fun mapRow(rs: ResultSet, rowNum: Int): Coffee =
            Coffee(
                    rs.getLong("кофе.id"),
                    rs.getString("название"),
                    rs.getDouble("стоимость"),
                    rs.getBytes("фото"),
                    rs.getString("тип").first(),
                    rs.getString("состояние"),
                    userRepository.findById(rs.getLong("id_автора")).get(),
                    coffeeComponentRepository.findAllByCoffeeId(rs.getLong("id")),
            )
}