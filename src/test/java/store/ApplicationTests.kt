package store

import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import coffee.store.dao.Address
import coffee.store.dao.User
import coffee.store.entity.Sex
import coffee.store.repository.UserRepository
import java.time.LocalDate

@SpringBootTest
class ApplicationTests(
        private val userRepository: UserRepository
) {
    @Test
    fun customerRepositoryTest() {
        val address = Address(0, "russia", null, null, null, null, null, null, null, null, null)
        val customer1 = User(0, "Georgii", "Savin", Sex.M, null, null, null, "123123")
        val customer2 = User(0, "Georgii", "Savin", Sex.M, LocalDate.now(), address, "sadf@dsadasd.sa", "123123")
        userRepository.save(customer1)
        userRepository.save(customer2)
    }

}
