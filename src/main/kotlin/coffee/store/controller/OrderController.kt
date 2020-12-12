package coffee.store.controller

import coffee.store.entity.CoffeeStore
import coffee.store.entity.Dessert
import coffee.store.entity.Order
import coffee.store.entity.OrderItem
import coffee.store.model.CoffeeType
import coffee.store.model.OrderStatus
import coffee.store.model.ProductType
import coffee.store.model.ScheduleStatus
import coffee.store.payload.request.order.SubmitOrderRequest
import coffee.store.payload.response.DessertListItemResponse
import coffee.store.payload.response.MessageResponse
import coffee.store.payload.response.coffee.CoffeeListItemResponse
import coffee.store.payload.response.schedule.ScheduleListItemResponse
import coffee.store.repository.*
import coffee.store.service.CoffeeService
import coffee.store.service.ScheduleService
import coffee.store.service.UserService
import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*
import java.time.LocalDateTime
import javax.persistence.EntityNotFoundException

@CrossOrigin(origins = ["*"], maxAge = 3600)
@RestController
@RequestMapping("/api/order")
@Api(tags = ["Order"])
class OrderController(
        private val coffeeStoreJpaRepository: CoffeeStoreJpaRepository,
        private val orderJpaRepository: OrderJpaRepository,
        private val orderComponentJpaRepository: OrderComponentJpaRepository,
        private val coffeeService: CoffeeService,
        private val coffeeJpaRepository: CoffeeJpaRepository,
        private val dessertsJpaRepository: DessertJpaRepository,
        private val scheduleJpaRepository: ScheduleJpaRepository,
        private val userService: UserService,
        private val scheduleService: ScheduleService,
) {
    @GetMapping("stores")
    fun getCoffeeStores(): List<CoffeeStore> = coffeeStoreJpaRepository.findAll().toList()

    @ApiOperation("Get public coffees")
    @GetMapping("coffees")
    fun getCoffees(): List<CoffeeListItemResponse> = coffeeJpaRepository.findAllPublicCoffee()
            .map { c ->
                CoffeeListItemResponse(c.id, c.name, c.cost,
                        CoffeeType.valueOf(c.type), c.avgRating, c.photo)
            }

    @GetMapping("coffees/{id}")
    fun getCoffee(@PathVariable id: Long) = coffeeService.getCoffee(id)

    @ApiOperation("Get public schedules")
    @GetMapping("schedules")
    fun getSchedules(): List<ScheduleListItemResponse> = scheduleJpaRepository.findAllPublicSchedules()
            .map { c ->
                ScheduleListItemResponse(c.id, c.name, c.description,
                        c.avgRating, ScheduleStatus.valueOf(c.status))
            }

    @GetMapping("schedule/{id}")
    fun getSchedule(@PathVariable id: Long) = scheduleService.getSchedule(id)

    @GetMapping("desserts")
    fun getDesserts(): List<DessertListItemResponse> = dessertsJpaRepository.findAll()
            .map { d -> DessertListItemResponse(d.id, d.name, d.cost, d.photo) }

    @GetMapping("desserts/{id}")
    fun getDessert(@PathVariable id: Long): Dessert =
            dessertsJpaRepository.findById(id).orElseThrow { EntityNotFoundException("Dessert not found - $id") }

    @PostMapping
    @PreAuthorize("hasRole('CUSTOMER')")
    fun submitOrder(@RequestBody order: SubmitOrderRequest): MessageResponse {
        val user = userService.getUserFromAuth()
        val store = coffeeStoreJpaRepository.findById(order.coffeeStoreId)
                .orElseThrow { EntityNotFoundException("Coffee store not found - ${order.coffeeStoreId}") }
        var sum = 0.0
        val newOrder = Order(status = OrderStatus.FORMING, user = user, coffeeStore = store,
                discount = 0.0, orderTime = LocalDateTime.now(), cost = sum)
        val orderItemList = order.orderItems.map { i ->
            val product = if (i.type == ProductType.Coffee) {
                coffeeJpaRepository.findById(i.productId)
                        .orElseThrow { EntityNotFoundException("Coffee not found - ${i.productId}") }
            } else {
                dessertsJpaRepository.findById(i.productId)
                        .orElseThrow { EntityNotFoundException("Dessert not found - ${i.productId}") }
            }
            sum += product.cost * i.quantity
            OrderItem(0, newOrder, product, i.quantity)
        }
        newOrder.cost = sum
        orderJpaRepository.save(newOrder)
        orderItemList.forEach { i -> orderComponentJpaRepository.save(i) }
        return MessageResponse("Order successfully submitted!")
    }
}