package coffee.store.controller

import coffee.store.entity.Coffee
import coffee.store.model.ProductType
import coffee.store.payload.common.UserInformationPayload
import coffee.store.payload.request.IdListRequest
import coffee.store.payload.request.SubmitOrderItem
import coffee.store.payload.response.*
import coffee.store.repository.CoffeeJpaRepository
import coffee.store.repository.OrderJpaRepository
import coffee.store.repository.ScheduleJpaRepository
import coffee.store.service.UserService
import io.swagger.annotations.Api
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*
import javax.persistence.EntityNotFoundException
import javax.transaction.Transactional

@CrossOrigin(origins = ["*"], maxAge = 3600)
@RestController
@RequestMapping("/api/customer")
@Api(tags = ["Customer"])
@PreAuthorize("hasRole('CUSTOMER')")
class CustomerController(
        private val orderJpaRepository: OrderJpaRepository,
        private val coffeeJpaRepository: CoffeeJpaRepository,
        private val scheduleJpaRepository: ScheduleJpaRepository,
        private val userService: UserService,
) {

    @GetMapping
    fun getUserInformation(): UserInformationPayload {
        val user = userService.getUserFromAuth()
        return UserInformationPayload(user.firstName, user.lastName, user.sex, user.birthDay,
                user.address, user.email, user.phone, user.password)
    }

    @PutMapping
    fun editUser(@RequestBody payload: UserInformationPayload): MessageResponse {
        userService.editUser(payload)
        return MessageResponse("Successfully updated user information")
    }

    @GetMapping("orders")
    fun getOrders(): List<OrderListItemResponse> =
            orderJpaRepository.findOrderByUser(userService.getCurrentUserId())
                    .map { o -> OrderListItemResponse(o.id, o.status, o.cost, o.discount, o.orderTime) }

    @GetMapping("order/{id}")
    @Transactional
    fun getOrder(@PathVariable id: Long): OrderFullItemResponse {
        val order = orderJpaRepository.findById(id)
                .orElseThrow { EntityNotFoundException("Order not found - $id") }
        userService.checkAuthority(order)
        return OrderFullItemResponse(order.id, order.status, order.coffeeStore, order.discount,
                order.cost, order.orderTime, order.items.asIterable().map { i ->
            SubmitOrderItem(i.id, i.quantity, if (i.product is Coffee) ProductType.Coffee else ProductType.Dessert)
        })
    }

    @GetMapping("favorite_coffees")
    @Transactional
    fun getFavoriteCoffees(): List<CoffeeListItemResponse> =
            userService.getUserFromAuth().favoriteCoffees.asIterable()
                    .map { c -> CoffeeListItemResponse(c.id, c.name, c.cost, c.type, null, c.photo) }

    @PostMapping("favorite_coffees")
    @Transactional
    fun addFavoriteCoffees(@RequestBody adding: IdListRequest): MessageResponse {
        val user = userService.getUserFromAuth()
        val newFavoriteCoffees = user.favoriteCoffees.plus(
                adding.ids.map { i -> coffeeJpaRepository.findById(i).orElseThrow { EntityNotFoundException("Coffee not found - $i") } })
        user.favoriteCoffees = newFavoriteCoffees
        return MessageResponse("Successfully added to favorite coffees!")
    }

    @DeleteMapping("favorite_coffees")
    @Transactional
    fun removeFavoriteCoffees(@RequestBody adding: IdListRequest): MessageResponse {
        val user = userService.getUserFromAuth()
        val newFavoriteCoffees = user.favoriteCoffees.minus(
                adding.ids.map { i -> coffeeJpaRepository.findById(i).orElseThrow { EntityNotFoundException("Coffee not found - $i") } })
        user.favoriteCoffees = newFavoriteCoffees
        return MessageResponse("Successfully removed from favorite coffees!")
    }

    @GetMapping("favorite_schedules")
    @Transactional
    fun getFavoriteSchedules(): List<ScheduleListItemResponse> =
            userService.getUserFromAuth().favoriteSchedules.asIterable()
                    .map { s -> ScheduleListItemResponse(s.id, s.name, s.description, null, s.status) }

    @PostMapping("favorite_schedules")
    @Transactional
    fun addFavoriteSchedules(@RequestBody adding: IdListRequest): MessageResponse {
        val user = userService.getUserFromAuth()
        val newFavoriteSchedules = user.favoriteSchedules.plus(
                adding.ids.map { i -> scheduleJpaRepository.findById(i).orElseThrow { EntityNotFoundException("Schedule not found - $i") } })
        user.favoriteSchedules = newFavoriteSchedules
        return MessageResponse("Successfully added to favorite schedules!")
    }

    @DeleteMapping("favorite_schedules")
    @Transactional
    fun removeFavoriteSchedules(@RequestBody adding: IdListRequest): MessageResponse {
        val user = userService.getUserFromAuth()
        val newFavoriteSchedules = user.favoriteSchedules.minus(
                adding.ids.map { i -> scheduleJpaRepository.findById(i).orElseThrow { EntityNotFoundException("Schedule not found - $i") } })
        user.favoriteSchedules = newFavoriteSchedules
        return MessageResponse("Successfully removed from favorite schedules!")
    }

}