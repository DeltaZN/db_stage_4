package coffee.store

import coffee.store.auth.ERole
import coffee.store.entity.Address
import coffee.store.entity.User
import coffee.store.model.Sex
import coffee.store.repository.AddressJpaRepository
import coffee.store.repository.RoleJpaRepository
import coffee.store.repository.UserJpaRepository
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.security.crypto.password.PasswordEncoder
import java.time.LocalDate


fun main(args: Array<String>) {
    runApplication<Application>(*args)
}

@SpringBootApplication
class Application(
        private val userRepository: UserJpaRepository,
        private val addressJpaRepository: AddressJpaRepository,
        private val roleJpaRepository: RoleJpaRepository,
        private val encoder: PasswordEncoder,
) : CommandLineRunner {

    override fun run(vararg args: String?) {
        val address = Address(0, "russia", null, null, null, null, null, null, null, null, null)
        val customer1 = User(0, "Georgii", "Savin", Sex.M, null, null, null, "123123", encoder.encode("1234"), setOf(roleJpaRepository.findRoleByName(ERole.ROLE_CUSTOMER).get()))
        val customer2 = User(0, "Georgii", "Savin", Sex.M, LocalDate.now(), address, "sadf@dsadasd.sa", "1123123", encoder.encode("1234"), setOf(roleJpaRepository.findRoleByName(ERole.ROLE_CUSTOMER).get(), roleJpaRepository.findRoleByName(ERole.ROLE_BARISTA).get()))
        addressJpaRepository.save(address)
        userRepository.save(customer2)
        userRepository.save(customer1)
        userRepository.findByPhone("123123")
    }

}
