package ru.itmo.coffee.store.repository.impl

import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource
import org.springframework.jdbc.core.namedparam.SqlParameterSource
import org.springframework.jdbc.core.simple.SimpleJdbcInsert
import org.springframework.stereotype.Repository
import ru.itmo.coffee.store.dao.Address
import ru.itmo.coffee.store.repository.AddressRepository
import ru.itmo.coffee.store.repository.mapper.AddressMapper
import javax.annotation.PostConstruct
import javax.sql.DataSource


@Repository
class JdbcAddressRepository(private val jdbcTemplate: JdbcTemplate, private val rowMapper: AddressMapper,
                            private val dataSource: DataSource) : AddressRepository {

    private lateinit var jdbcInsert: SimpleJdbcInsert
    private val cache: HashMap<Long, Address> = HashMap()

    @PostConstruct
    fun init() {
        jdbcInsert = SimpleJdbcInsert(dataSource)
                .withTableName("адрес").usingGeneratedKeyColumns("id")
    }

    override fun save(address: Address): Long {
        val parameters = HashMap<String, Any?>()
        parameters["страна"] = address.country
        parameters["субъект"] = address.subject
        parameters["муниципальный_район"] = address.municipalDistrict
        parameters["поселение"] = address.settlement
        parameters["населенный_пункт"] = address.humanSettlement
        parameters["планировчная_структура"] = address.planningStructure
        parameters["улица"] = address.street
        parameters["номер_земельного_участка"] = address.landPlot
        parameters["номер_здания"] = address.building
        parameters["номер_помещения"] = address.room
        address.id = jdbcInsert.executeAndReturnKey(parameters).toLong()
        cache[address.id] = address
        return address.id
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
        cache.remove(id)
        return jdbcTemplate.update(
                "delete from адрес where id = ?", id)
    }

    override fun findAll(): List<Address> {
        return jdbcTemplate.query(
                "select * from адрес", rowMapper)
    }

    override fun findById(id: Long): Address? {
        return if (cache[id] != null) {
            cache[id]
        } else {
            val res = jdbcTemplate.queryForObject(
                    "select * from адрес where id = ?", rowMapper)
            if (res != null) {
                cache[res.id] = res
            }
            res
        }
    }
}