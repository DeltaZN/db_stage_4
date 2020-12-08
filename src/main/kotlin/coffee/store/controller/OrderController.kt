package coffee.store.controller

import coffee.store.entity.*
import coffee.store.model.CoffeeType
import coffee.store.model.OrderStatus
import coffee.store.model.ProductType
import coffee.store.model.ScheduleStatus
import coffee.store.payload.request.SubmitOrderItem
import coffee.store.payload.request.SubmitOrderRequest
import coffee.store.payload.response.*
import coffee.store.repository.*
import coffee.store.service.UserService
import io.swagger.annotations.Api
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.*
import java.time.LocalDateTime
import javax.persistence.EntityNotFoundException
import javax.transaction.Transactional

@CrossOrigin(origins = ["*"], maxAge = 3600)
@RestController
@RequestMapping("/api/order")
@Api(tags = ["Order"])
class OrderController(
        private val coffeeStoreJpaRepository: CoffeeStoreJpaRepository,
        private val coffeeScoreJpaRepository: CoffeeScoreJpaRepository,
        private val coffeeJpaRepository: CoffeeJpaRepository,
        private val dessertsJpaRepository: DessertJpaRepository,
        private val scheduleJpaRepository: ScheduleJpaRepository,
        private val scheduleScoreJpaRepository: ScheduleScoreJpaRepository,
        private val userService: UserService,
) {
    @GetMapping("stores")
    fun getCoffeeStores(): List<CoffeeStore> = coffeeStoreJpaRepository.findAll().toList()

    @GetMapping("coffees")
    fun getCoffees(): List<CoffeeListItemResponse> = coffeeJpaRepository.findAllPublicCoffee()
            .map { c ->
                CoffeeListItemResponse(c.id, c.name, c.cost,
                        CoffeeType.valueOf(c.type), c.avgRating, c.photo)
            }

    @GetMapping("schedules")
    fun getSchedules(): List<ScheduleListItemResponse> = scheduleJpaRepository.findAllPublicSchedules()
            .map { c ->
                ScheduleListItemResponse(c.id, c.name, c.description,
                        c.avgRating, ScheduleStatus.valueOf(c.status))
            }

    @GetMapping("coffees/{id}")
    @Transactional
    fun getCoffee(@PathVariable id: Long): CoffeeFullItemResponse {
        val coffee = coffeeJpaRepository.findById(id)
                .orElseThrow { EntityNotFoundException("Coffee not found - $id") }
        val components = coffee.components.asIterable()
                .map { c ->
                    CoffeeFullItemComponent(c.ingredient.name, c.addingOrder,
                            c.quantity, c.ingredient.volumeMl)
                }
        return CoffeeFullItemResponse(coffee.id, coffee.name, coffee.cost, coffee.type,
                "${coffee.author?.firstName} ${coffee.author?.lastName}",
                coffeeScoreJpaRepository.getAverageScoreByCoffee(coffee.id), coffee.photo, components)
    }

    @GetMapping("schedules/{id}")
    @Transactional
    fun getSchedule(@PathVariable id: Long): ScheduleFullItemResponse {
        val schedule = scheduleJpaRepository.findById(id).orElseThrow { EntityNotFoundException("Coffee not found - $id") }
        val components = schedule.components.asIterable()
                .map { c ->
                    val items = c.order.items.asIterable().map { i ->
                        SubmitOrderItem(i.product.id, i.quantity, if (i.product is Coffee) ProductType.Coffee else ProductType.Dessert)
                    }
                    ScheduleFullItemComponent(c.name, items, c.dayOfWeek, c.time)
                }
        return ScheduleFullItemResponse(schedule.id, schedule.name,
                "${schedule.author.firstName} ${schedule.author.lastName}",
                scheduleScoreJpaRepository.getAverageScoreBySchedule(schedule.id),
                schedule.description, schedule.status, components)
    }

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
            OrderItem(0, newOrder, product)
        }
        newOrder.items = orderItemList
        return MessageResponse("Order successfully submitted!")
    }
}