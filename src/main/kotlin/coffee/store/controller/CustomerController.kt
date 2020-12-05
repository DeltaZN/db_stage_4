package coffee.store.controller

import coffee.store.auth.UserDetailsImpl
import coffee.store.payload.response.OrderListItemResponse
import coffee.store.repository.OrderJpaRepository
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@CrossOrigin(origins = ["*"], maxAge = 3600)
@RestController
@RequestMapping("/api/customer")
@PreAuthorize("hasRole('CUSTOMER')")
class CustomerController(
        private val orderJpaRepository: OrderJpaRepository,
) {
    @GetMapping("orders")
    fun getOrders(auth: Authentication): List<OrderListItemResponse> =
            orderJpaRepository.findOrderByUser((auth.details as UserDetailsImpl).id)
                    .map { o -> OrderListItemResponse(o.id, o.status, o.cost, o.discount, o.orderTime) }

    @GetMapping("custom_coffees")
    fun getCustomCoffees(auth: Authentication) {
    }

    @GetMapping("custom_schedules")
    fun getCustomSchedules(auth: Authentication) {
    }

    @GetMapping("favorite_coffees")
    fun getFavoriteCoffees(auth: Authentication) {
    }

    @GetMapping("favorite_schedules")
    fun getFavoriteSchedules(auth: Authentication) {
    }
}