package ru.itmo.coffee.store.repository

import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Repository
import ru.itmo.coffee.store.model.Address

@Repository
class JdbcAddressRepository(private val jdbcTemplate: JdbcTemplate) : AddressRepository {
    override fun save(address: Address): Int {
        TODO("Not yet implemented")
    }

    override fun update(address: Address): Int {
        TODO("Not yet implemented")
    }

    override fun deleteById(id: Long): Int {
        TODO("Not yet implemented")
    }

    override fun findAll(): List<Address> {
        TODO("Not yet implemented")
    }

    override fun findById(id: Long): Address? {
        return jdbcTemplate.queryForObject(
                "select * from адрес where id = ?", arrayOf<Any>(id)
        ) { rs, _ ->
            Address(
                    rs.getLong("id"),
                    rs.getString("страна"),
                    rs.getString("субъект"),
                    rs.getString("муниципальный_район"),
                    rs.getString("поселение"),
                    rs.getString("населенный_пункт"),
                    rs.getString("планировочная_структура"),
                    rs.getString("улица"),
                    rs.getString("номер_земельного_участка"),
                    rs.getString("номер_здания"),
                    rs.getString("номер_помещения"),
            )
        }
    }
}