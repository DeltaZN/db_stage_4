package ru.itmo.coffee.store.repository.impl

import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.simple.SimpleJdbcInsert
import org.springframework.stereotype.Repository
import ru.itmo.coffee.store.dao.Address
import ru.itmo.coffee.store.dao.Customer
import ru.itmo.coffee.store.repository.AddressRepository
import ru.itmo.coffee.store.repository.CustomerRepository
import ru.itmo.coffee.store.repository.mapper.CustomerMapper
import javax.annotation.PostConstruct
import javax.sql.DataSource


@Repository
class JdbcCustomerRepository(
        private val jdbcTemplate: JdbcTemplate,
        private val rowMapper: CustomerMapper,
        private val addressRepository: AddressRepository,
        private val dataSource: DataSource
) : CustomerRepository {

    private lateinit var jdbcInsert: SimpleJdbcInsert
    private val cache: HashMap<Long, Address> = HashMap()

    @PostConstruct
    fun init() {
        jdbcInsert = SimpleJdbcInsert(dataSource)
                .withTableName("клиент").usingGeneratedKeyColumns("id");
    }

    override fun save(customer: Customer): Long {
        customer.address?.let {
            if (it.id != 0L) addressRepository.update(it)
            else addressRepository.save(it)
        }
        val parameters = HashMap<String, Any?>()
        parameters["имя"] = customer.firstName
        parameters["фамилия"] = customer.lastName
        parameters["пол"] = customer.sex
        parameters["дата_рождения"] = customer.birthDay
        parameters["id_адреса"] = customer.address?.id
        parameters["email"] = customer.email
        parameters["телефон"] = customer.phone
        customer.id = jdbcInsert.executeAndReturnKey(parameters).toLong()
        return customer.id
    }

    override fun update(customer: Customer): Int {
        customer.address?.let { addressRepository.save(it) }
        return jdbcTemplate.update(
                "update клиент set имя = ?, фамилия = ?, пол = ?, дата_рождения = ?, id_адреса = ?, email = ?, телефон = ? where id = ?",
                customer.firstName, customer.lastName, customer.sex, customer.birthDay,
                customer.address?.id, customer.email, customer.phone, customer.id)
    }

    override fun deleteById(id: Long): Int {
        return jdbcTemplate.update(
                "delete from клиент where id = ?", id)
    }

    override fun findAll(): List<Customer> {
        return jdbcTemplate.query("select * from клиент", rowMapper)
    }

    override fun findById(id: Long): Customer? {
        return jdbcTemplate.queryForObject(
                "select * from клиент where id = ?", rowMapper)
    }
}