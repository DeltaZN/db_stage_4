package coffee.store.controller

import coffee.store.dao.User
import coffee.store.repository.jpa.UserJpaRepository
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@CrossOrigin(origins = ["*"], maxAge = 3600)
@RestController
@RequestMapping("/api/admin")
class AdminController(
        private val userJpaRepository: UserJpaRepository,
) {
    @GetMapping("users")
    fun getUsers(): MutableIterable<User> = userJpaRepository.findAll()
}