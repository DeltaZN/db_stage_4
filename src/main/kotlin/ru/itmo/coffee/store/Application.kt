package ru.itmo.coffee.store

import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import ru.itmo.coffee.store.dao.Address
import ru.itmo.coffee.store.dao.Customer
import ru.itmo.coffee.store.model.Sex
import ru.itmo.coffee.store.repository.CustomerRepository
import ru.itmo.coffee.store.util.DatabaseUtils
import java.time.LocalDate

fun main(args: Array<String>) {
    runApplication<Application>(*args)
}

@SpringBootApplication
class Application(private val customerRepository: CustomerRepository,
                  private val databaseUtils: DatabaseUtils) : CommandLineRunner {

    override fun run(vararg args: String?) {
        databaseUtils.dropTables()
        databaseUtils.createTables()
        val address = Address(0, "russia", null, null, null, null, null, null, null, null, null)
        val customer1 = Customer(0, "Georgii", "Savin", Sex.M, null, null, null, "123123")
        val customer2 = Customer(0, "Georgii", "Savin", Sex.M, LocalDate.now(), address, "sadf@dsadasd.sa", "1123123")
        customerRepository.save(customer1)
        customerRepository.save(customer2)
    }

}
