package ru.itmo.coffee.store

import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import ru.itmo.coffee.store.dao.Address
import ru.itmo.coffee.store.dao.Customer
import ru.itmo.coffee.store.model.Sex
import ru.itmo.coffee.store.repository.CustomerRepository
import ru.itmo.coffee.store.util.DatabaseUtils
import java.time.LocalDate

@SpringBootTest
class ApplicationTests(private val databaseUtils: DatabaseUtils,
                       private val customerRepository: CustomerRepository) {

    @BeforeAll
    fun init() {
        databaseUtils.dropTables()
        databaseUtils.createTables()
    }

    @Test
    fun customerRepositoryTest() {
        val address = Address(0, "russia", null, null, null, null, null, null, null, null, null)
        val customer1 = Customer(0, "Georgii", "Savin", Sex.M, null, null, null, "123123")
        val customer2 = Customer(0, "Georgii", "Savin", Sex.M, LocalDate.now(), address, "sadf@dsadasd.sa", "123123")
        customerRepository.save(customer1)
        customerRepository.save(customer2)
    }

}
