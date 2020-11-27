package ru.itmo.coffee.store.repository.mapper

import org.springframework.jdbc.core.RowMapper
import ru.itmo.coffee.store.model.Dessert
import java.sql.ResultSet

class DessertMapper : RowMapper<Dessert> {
    override fun mapRow(rs: ResultSet, rowNum: Int): Dessert =
            Dessert(
                    rs.getLong("id"),
                    rs.getDouble("калории"),
                    rs.getDouble("вес"),
                    rs.getString("название"),
                    rs.getDouble("стоимость"),
                    rs.getBytes("фото"),
            )
}