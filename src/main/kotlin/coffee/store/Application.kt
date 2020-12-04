package coffee.store

import coffee.store.auth.ERole
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import coffee.store.dao.Address
import coffee.store.dao.Dessert
import coffee.store.dao.User
import coffee.store.entity.Sex
import coffee.store.repository.jpa.AddressJpaRepository
import coffee.store.repository.UserRepository
import coffee.store.repository.jpa.DessertJpaRepository
import org.springframework.security.crypto.password.PasswordEncoder
import java.time.LocalDate

fun main(args: Array<String>) {
    runApplication<Application>(*args)
}

@SpringBootApplication
class Application(
        private val userRepository: UserRepository,
        private val dessertJpaRepository: DessertJpaRepository,
        private val encoder: PasswordEncoder,
) : CommandLineRunner {

    override fun run(vararg args: String?) {
        val address = Address(0, "russia", null, null, null, null, null, null, null, null, null)
        val customer1 = User(0, "Georgii", "Savin", Sex.M, null, null, null, "123123", encoder.encode("1234"), ERole.ROLE_CUSTOMER)
        val customer2 = User(0, "Georgii", "Savin", Sex.M, LocalDate.now(), address, "sadf@dsadasd.sa", "1123123", encoder.encode("1234"), ERole.ROLE_BARISTA)
        userRepository.save(customer2)
        userRepository.save(customer1)
        userRepository.findByPhone("123123")
        dessertJpaRepository.save(Dessert(calories = 1.0, weight = 2.0, name = "TEST", cost = 10.0))
    }

}
