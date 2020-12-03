package coffee.store.controller

import coffee.store.dao.Address
import coffee.store.repository.impl.JdbcAddressRepository
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*
import java.util.*


@CrossOrigin(origins = ["*"], maxAge = 3600)
@RestController
@RequestMapping("/api/test")
class TestController(
        private val addresses: JdbcAddressRepository
) {

    @GetMapping("/all")
    fun allAccess(): String {
        return "Public Content."
    }

    @GetMapping("/user")
    @PreAuthorize("hasRole('CUSTOMER') or hasRole('BARISTA') or hasRole('ADMIN')")
    fun userAccess(): String {
        return "User Content."
    }

    @GetMapping("/barista")
    @PreAuthorize("hasRole('BARISTA') or hasRole('ADMIN')")
    fun baristaAccess(): String {
        return "Barista Content."
    }

    @GetMapping("/admin")
    @PreAuthorize("hasRole('ADMIN')")
    fun adminAccess(): String {
        return "Barista Content."
    }

    @GetMapping("/{id}")
    fun test(@PathVariable id: Long): Optional<Address> {
        return addresses.findById(id)
    }

    @GetMapping("/mnogo")
    fun test2(): List<Address> {
        return addresses.findAll()
    }
}