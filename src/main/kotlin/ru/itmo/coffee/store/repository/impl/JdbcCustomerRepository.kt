package ru.itmo.coffee.store.repository.impl

import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Repository
import ru.itmo.coffee.store.dao.Customer
import ru.itmo.coffee.store.repository.AddressRepository
import ru.itmo.coffee.store.repository.CustomerRepository
import ru.itmo.coffee.store.repository.mapper.CustomerMapper


@Repository
class JdbcCustomerRepository(private val jdbcTemplate: JdbcTemplate, private val rowMapper: CustomerMapper,
    private val addressRepository: AddressRepository) : CustomerRepository {

    override fun save(customer: Customer): Int {
        customer.address?.let { addressRepository.save(it) }
        return jdbcTemplate.update(
                "insert into клиент (имя, фамилия, пол, дата_рождения, id_адреса, email, телефон) values (?,?,?,?,?,?,?)",
                customer.firstName, customer.lastName, customer.sex.toString(), customer.birthDay,
                customer.address?.id, customer.email, customer.phone)
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