package coffee.store.controller

import coffee.store.entity.CoffeeStore
import coffee.store.payload.request.ProcessOrderRequest
import coffee.store.payload.response.MessageResponse
import coffee.store.payload.response.OrderListItemResponse
import coffee.store.repository.CoffeeStoreJpaRepository
import coffee.store.repository.OrderJpaRepository
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*

@CrossOrigin(origins = ["*"], maxAge = 3600)
@RestController
@RequestMapping("/api/barista")
class BaristaController(
        private val coffeeStoreJpaRepository: CoffeeStoreJpaRepository,
        private val orderJpaRepository: OrderJpaRepository,
) {
    @GetMapping("orders/{coffeeStoreId}")
    @PreAuthorize("hasRole('BARISTA')")
    fun getIncompleteOrdersByCoffeeStore(@PathVariable coffeeStoreId: Long): List<OrderListItemResponse> =
            orderJpaRepository.findIncompleteOrders(coffeeStoreId)
                    .map { o -> OrderListItemResponse(o.id, o.status, o.cost, o.discount, o.orderTime) }

    @PostMapping("orders/{orderId}")
    @PreAuthorize("hasRole('BARISTA')")
    fun processOrder(@PathVariable orderId: Long, request: ProcessOrderRequest): MessageResponse {
        val order = orderJpaRepository.findIncompleteOrder(orderId)
                .orElseThrow { Exception("order not found - $orderId") }
        order.status = request.newStatus
        orderJpaRepository.save(order)
        return MessageResponse("Successfully processed order, new status - ${order.status}")
    }
}