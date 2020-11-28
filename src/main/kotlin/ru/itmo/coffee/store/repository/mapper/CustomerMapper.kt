package ru.itmo.coffee.store.repository.mapper

import org.springframework.jdbc.core.RowMapper
import org.springframework.stereotype.Component
import ru.itmo.coffee.store.dao.Customer
import ru.itmo.coffee.store.model.Sex
import ru.itmo.coffee.store.repository.AddressRepository
import java.sql.ResultSet

@Component
class CustomerMapper(private val addressRepository: AddressRepository) : RowMapper<Customer> {
    override fun mapRow(rs: ResultSet, rowNum: Int): Customer =
            Customer(
                    rs.getLong("id"),
                    rs.getString("имя"),
                    rs.getString("фамилия"),
                    Sex.valueOf(rs.getString("пол")),
                    rs.getDate("дата_рождения").toLocalDate(),
                    addressRepository.findById(rs.getLong("id_адреса")),
                    rs.getString("email"),
                    rs.getString("телефон"),
            )
}