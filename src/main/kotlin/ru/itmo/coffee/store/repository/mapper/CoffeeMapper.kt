package ru.itmo.coffee.store.repository.mapper

import org.springframework.jdbc.core.RowMapper
import org.springframework.stereotype.Component
import ru.itmo.coffee.store.dao.Coffee
import ru.itmo.coffee.store.repository.CustomerRepository
import java.sql.ResultSet

@Component
class CoffeeMapper(private val customerRepository: CustomerRepository) : RowMapper<Coffee> {
    override fun mapRow(rs: ResultSet, rowNum: Int): Coffee =
            Coffee(
                    rs.getLong("id"),
                    rs.getString("название"),
                    rs.getDouble("стоимость"),
                    rs.getBytes("фото"),
                    rs.getString("тип").first(),
                    rs.getString("состояние"),
                    customerRepository.findById(rs.getLong("id_автора")),
            )
}