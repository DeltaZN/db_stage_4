package ru.itmo.coffee.store.repository.mapper

import org.springframework.jdbc.core.RowMapper
import org.springframework.stereotype.Component
import ru.itmo.coffee.store.dao.CoffeeStore
import ru.itmo.coffee.store.repository.AddressRepository
import java.sql.ResultSet

@Component
class CoffeeStoreMapper(private val addressRepository: AddressRepository) : RowMapper<CoffeeStore> {
    override fun mapRow(rs: ResultSet, rowNum: Int): CoffeeStore =
            CoffeeStore(
                    rs.getLong("id"),
                    addressRepository.findById(rs.getLong("id_адреса")),
                    rs.getString("телефон"),
            )
}