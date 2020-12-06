package coffee.store.controller

import coffee.store.entity.Address
import coffee.store.payload.response.MessageResponse
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
    fun allAccess() = MessageResponse("Public Content.")


    @GetMapping("/user")
    @PreAuthorize("hasRole('CUSTOMER')")
    fun userAccess() = MessageResponse("User Content.")

    @GetMapping("/barista")
    @PreAuthorize("hasRole('BARISTA')")
    fun baristaAccess() = MessageResponse("Barista Content.")

    @GetMapping("/admin")
    @PreAuthorize("hasRole('ADMIN')")
    fun adminAccess() = MessageResponse("Admin Content.")

    @GetMapping("/{id}")
    fun test(@PathVariable id: Long): Optional<Address> {
        return addresses.findById(id)
    }
}