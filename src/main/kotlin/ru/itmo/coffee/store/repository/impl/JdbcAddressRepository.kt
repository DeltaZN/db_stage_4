package ru.itmo.coffee.store.repository.impl

import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Repository
import ru.itmo.coffee.store.model.Address
import ru.itmo.coffee.store.repository.AddressRepository
import ru.itmo.coffee.store.repository.mapper.AddressMapper

@Repository
class JdbcAddressRepository(private val jdbcTemplate: JdbcTemplate, private val rowMapper: AddressMapper) : AddressRepository {
    override fun save(address: Address): Int {
        return jdbcTemplate.update(
                "insert into адрес (страна, субъект, муниципальный_район, поселение," +
                        "населенный_пункт, планировочная_структура, улица, номер_земельного_участка," +
                        "номер_здания, номер_помещения) values(?,?,?,?,?,?,?,?,?,?)",
                address.country, address.subject,
                address.municipalDistrict, address.settlement,
                address.humanSettlement, address.planningStructure,
                address.street, address.landPlot,
                address.building, address.room)
    }

    override fun update(address: Address): Int {
        return jdbcTemplate.update(
                "update адрес set страна = ?, субъект = ?, муниципальный_район = ?, поселение = ?," +
                        " населенный_пункт = ?, планировочная_структура = ?, улица = ?, номер_земельного_участка = ?," +
                        " номер_здания = ?, номер_помещения = ? where id = ?",
                address.country, address.subject,
                address.municipalDistrict, address.settlement,
                address.humanSettlement, address.planningStructure,
                address.street, address.landPlot,
                address.building, address.room, address.id)
    }

    override fun deleteById(id: Long): Int {
        return jdbcTemplate.update(
                "delete from адрес where id = ?", id)
    }

    override fun findAll(): List<Address> {
        return jdbcTemplate.query(
                "select * from адрес", rowMapper)
    }

    override fun findById(id: Long): Address? {
        return jdbcTemplate.queryForObject(
                "select * from адрес where id = ?", rowMapper)
    }
}