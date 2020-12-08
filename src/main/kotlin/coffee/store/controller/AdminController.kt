package coffee.store.controller

import coffee.store.entity.User
import coffee.store.repository.UserJpaRepository
import io.swagger.annotations.Api
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@CrossOrigin(origins = ["*"], maxAge = 3600)
@RestController
@RequestMapping("/api/admin")
@Api(tags = ["Admin"])
@PreAuthorize("hasRole('ADMIN')")
class AdminController(
        private val userJpaRepository: UserJpaRepository,
) {
    @GetMapping("users")
    fun getUsers(): Iterable<User> = userJpaRepository.findAll()
}