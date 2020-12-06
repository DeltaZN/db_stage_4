package coffee.store.controller

import coffee.store.auth.UserDetailsImpl
import coffee.store.entity.CoffeeStore
import coffee.store.entity.Dessert
import coffee.store.entity.Order
import coffee.store.entity.OrderItem
import coffee.store.model.OrderStatus
import coffee.store.model.ProductType
import coffee.store.payload.request.SubmitOrderRequest
import coffee.store.payload.response.*
import coffee.store.repository.CoffeeJpaRepository
import coffee.store.repository.CoffeeStoreJpaRepository
import coffee.store.repository.DessertJpaRepository
import coffee.store.repository.UserJpaRepository
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.core.Authentication
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.web.bind.annotation.*
import java.time.LocalDateTime
import javax.persistence.EntityNotFoundException
import javax.transaction.Transactional

@CrossOrigin(origins = ["*"], maxAge = 3600)
@RestController
@RequestMapping("/api/order")
class OrderController(
        private val coffeeStoreJpaRepository: CoffeeStoreJpaRepository,
        private val coffeeJpaRepository: CoffeeJpaRepository,
        private val dessertsJpaRepository: DessertJpaRepository,
        private val customerJpaRepository: UserJpaRepository,
) {
    @GetMapping("stores")
    fun getCoffeeStores(): MutableIterable<CoffeeStore> = coffeeStoreJpaRepository.findAll()

    @GetMapping("coffees")
    fun getCoffees(): List<CoffeeListItemResponse> {
        // TODO добавить среднюю оценку
        return coffeeJpaRepository.findAllPublicCoffee()
                .map { c -> CoffeeListItemResponse(c.id, c.name, c.cost, c.type, null, c.photo) }
    }

    @GetMapping("coffees/{id}")
    @Transactional
    fun getCoffee(@PathVariable id: Long): CoffeeFullItemResponse {
        val coffee = coffeeJpaRepository.findById(id).orElseThrow { EntityNotFoundException("Coffee not found - $id") }
        val components = coffee.components.asIterable()
                .map { c -> CoffeeFullItemComponent(c.ingredient.name, c.addingOrder, c.quantity, c.ingredient.volumeMl) }
        // TODO добавить среднюю оценку
        return CoffeeFullItemResponse(coffee.id, coffee.name, coffee.cost, coffee.type,
                "${coffee.author?.firstName} ${coffee.author?.lastName}", null, null, components)
    }

    @GetMapping("desserts")
    fun getDesserts(): List<DessertListItemResponse> = dessertsJpaRepository.findAll()
            .map { d -> DessertListItemResponse(d.id, d.name, d.cost, d.photo) }

    @GetMapping("desserts/{id}")
    fun getDessert(@PathVariable id: Long): Dessert =
            dessertsJpaRepository.findById(id).orElseThrow { EntityNotFoundException("Dessert not found - $id") }

    @PostMapping
    @PreAuthorize("hasRole('CUSTOMER')")
    fun submitOrder(auth: Authentication, order: SubmitOrderRequest): MessageResponse {
        val user = customerJpaRepository.findById((auth.details as UserDetailsImpl).id)
                .orElseThrow { UsernameNotFoundException("${(auth.details as UserDetailsImpl).id}") }
        val store = coffeeStoreJpaRepository.findById(order.coffeeStoreId)
                .orElseThrow { EntityNotFoundException("Coffee store not found - ${order.coffeeStoreId}") }
        val productList = mutableListOf<OrderItem>()
        var sum = 0.0
        val newOrder = Order(status = OrderStatus.FORMING, user = user, coffeeStore = store,
                discount = 0.0, orderTime = LocalDateTime.now(), cost = sum, items = productList)
        order.orderItems.forEach { i ->
            val product = if (i.type == ProductType.Coffee) {
                coffeeJpaRepository.findById(i.productId)
                        .orElseThrow { EntityNotFoundException("Coffee not found - ${i.productId}") }
            } else {
                dessertsJpaRepository.findById(i.productId)
                        .orElseThrow { EntityNotFoundException("Dessert not found - ${i.productId}") }
            }
            productList.add(OrderItem(0, newOrder, product))
            sum += product.cost * i.quantity
        }
        return MessageResponse("Successfully submit order")
    }
}