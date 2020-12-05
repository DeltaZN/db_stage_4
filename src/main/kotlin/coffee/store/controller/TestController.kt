package coffee.store.controller

import coffee.store.entity.Address
import coffee.store.repository.AddressJpaRepository
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*
import java.util.*


@CrossOrigin(origins = ["*"], maxAge = 3600)
@RestController
@RequestMapping("/api/test")
class TestController(
        private val addresses: AddressJpaRepository
) {

    @GetMapping("/all")
    fun allAccess(): String {
        return "Public Content."
    }

    @GetMapping("/user")
    @PreAuthorize("hasRole('CUSTOMER')")
    fun userAccess(): String {
        return "User Content."
    }

    @GetMapping("/barista")
    @PreAuthorize("hasRole('BARISTA')")
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
}