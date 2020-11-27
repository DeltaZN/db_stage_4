package ru.itmo.coffee.store.repository

import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Repository
import ru.itmo.coffee.store.model.Customer
import java.sql.ResultSet


@Repository
class JdbcCustomerRepository(private val jdbcTemplate: JdbcTemplate) : CustomerRepository {

    override fun save(customer: Customer): Int {
        return jdbcTemplate.update("insert into клиент (имя, фамилия) values(?,?)", customer.firstName, customer.lastName)
    }

    override fun update(customer: Customer): Int {
        return jdbcTemplate.update(
                "update клиент set имя = ? where id = ?",
                customer.firstName, customer.id)
    }

    override fun deleteById(id: Long): Int {
        TODO("Not yet implemented")
    }

    override fun findAll(): List<Customer> {
        return jdbcTemplate.query(
                "select * from клиент"
        ) { rs: ResultSet, _: Int ->
            Customer(
                    rs.getLong("id"),
                    rs.getString("имя"),
                    rs.getString("фамилия")
            )
        }
    }

    override fun findById(id: Long): Customer? {
        TODO("Not yet implemented")
    }

    override fun getNameById(id: Long): String? {
        TODO("Not yet implemented")
    }
}