package coffee.store.controller

import coffee.store.auth.UserDetailsImpl
import coffee.store.payload.request.IdListRequest
import coffee.store.payload.response.CoffeeListItemResponse
import coffee.store.payload.response.MessageResponse
import coffee.store.payload.response.OrderListItemResponse
import coffee.store.payload.response.ScheduleListItemResponse
import coffee.store.repository.CoffeeJpaRepository
import coffee.store.repository.OrderJpaRepository
import coffee.store.repository.ScheduleJpaRepository
import coffee.store.repository.UserJpaRepository
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.core.Authentication
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.web.bind.annotation.*
import javax.persistence.EntityNotFoundException
import javax.transaction.Transactional

@CrossOrigin(origins = ["*"], maxAge = 3600)
@RestController
@RequestMapping("/api/customer")
@PreAuthorize("hasRole('CUSTOMER')")
class CustomerController(
        private val orderJpaRepository: OrderJpaRepository,
        private val coffeeJpaRepository: CoffeeJpaRepository,
        private val scheduleJpaRepository: ScheduleJpaRepository,
        private val userJpaRepository: UserJpaRepository,
) {

    private fun getUser(auth: Authentication) = userJpaRepository.findById((auth.principal as UserDetailsImpl).id)
            .orElseThrow { UsernameNotFoundException("User not found - ${(auth.principal as UserDetailsImpl).id}") }

    @GetMapping("orders")
    fun getOrders(auth: Authentication): List<OrderListItemResponse> =
            orderJpaRepository.findOrderByUser((auth.principal as UserDetailsImpl).id)
                    .map { o -> OrderListItemResponse(o.id, o.status, o.cost, o.discount, o.orderTime) }

    @GetMapping("favorite_coffees")
    @Transactional
    fun getFavoriteCoffees(auth: Authentication): List<CoffeeListItemResponse> =
            userJpaRepository.findById((auth.principal as UserDetailsImpl).id)
                    .orElseThrow { UsernameNotFoundException("User not found - ${(auth.principal as UserDetailsImpl).id}") }
                    .favoriteCoffees.asIterable()
                    .map { c -> CoffeeListItemResponse(c.id, c.name, c.cost, c.type, null, c.photo) }

    @PostMapping("favorite_coffees")
    @Transactional
    fun addFavoriteCoffees(auth: Authentication, adding: IdListRequest): MessageResponse {
        val user = getUser(auth)
        val newFavoriteCoffees = user.favoriteCoffees.plus(
                adding.ids.map { i -> coffeeJpaRepository.findById(i).orElseThrow { EntityNotFoundException("Coffee not found - $i") } })
        user.favoriteCoffees = newFavoriteCoffees
        return MessageResponse("Successfully added to favorite coffees!")
    }

    @DeleteMapping("favorite_coffees")
    @Transactional
    fun removeFavoriteCoffees(auth: Authentication, adding: IdListRequest): MessageResponse {
        val user = getUser(auth)
        val newFavoriteCoffees = user.favoriteCoffees.minus(
                adding.ids.map { i -> coffeeJpaRepository.findById(i).orElseThrow { EntityNotFoundException("Coffee not found - $i") } })
        user.favoriteCoffees = newFavoriteCoffees
        return MessageResponse("Successfully removed from favorite coffees!")
    }

    @GetMapping("favorite_schedules")
    @Transactional
    fun getFavoriteSchedules(auth: Authentication): List<ScheduleListItemResponse> =
            userJpaRepository.findById((auth.principal as UserDetailsImpl).id)
                    .orElseThrow { UsernameNotFoundException("User not found - ${(auth.principal as UserDetailsImpl).id}") }
                    .favoriteSchedules.asIterable()
                    .map { s -> ScheduleListItemResponse(s.id, s.name, s.description, s.status) }

    @PostMapping("favorite_schedules")
    @Transactional
    fun addFavoriteSchedules(auth: Authentication, adding: IdListRequest): MessageResponse {
        val user = getUser(auth)
        val newFavoriteSchedules = user.favoriteSchedules.plus(
                adding.ids.map { i -> scheduleJpaRepository.findById(i).orElseThrow { EntityNotFoundException("Schedule not found - $i") } })
        user.favoriteSchedules = newFavoriteSchedules
        return MessageResponse("Successfully added to favorite schedules!")
    }

    @DeleteMapping("favorite_schedules")
    @Transactional
    fun removeFavoriteSchedules(auth: Authentication, adding: IdListRequest): MessageResponse {
        val user = getUser(auth)
        val newFavoriteSchedules = user.favoriteSchedules.minus(
                adding.ids.map { i -> scheduleJpaRepository.findById(i).orElseThrow { EntityNotFoundException("Schedule not found - $i") } })
        user.favoriteSchedules = newFavoriteSchedules
        return MessageResponse("Successfully removed from favorite schedules!")
    }

}