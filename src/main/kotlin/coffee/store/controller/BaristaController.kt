package coffee.store.controller

import coffee.store.payload.request.ProcessOrderRequest
import coffee.store.payload.response.MessageResponse
import coffee.store.payload.response.OrderListItemResponse
import coffee.store.repository.OrderJpaRepository
import io.swagger.annotations.Api
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*
import javax.persistence.EntityNotFoundException

@CrossOrigin(origins = ["*"], maxAge = 3600)
@RestController
@RequestMapping("/api/barista")
@Api(tags = ["Barista"])
@PreAuthorize("hasRole('BARISTA')")
class BaristaController(
        private val orderJpaRepository: OrderJpaRepository,
) {
    @GetMapping("orders/{coffeeStoreId}")
    fun getIncompleteOrdersByCoffeeStore(@PathVariable coffeeStoreId: Long): List<OrderListItemResponse> =
            orderJpaRepository.findIncompleteOrders(coffeeStoreId)
                    .map { o -> OrderListItemResponse(o.id, o.status, o.cost, o.discount, o.orderTime) }

    @PostMapping("orders/{orderId}")
    fun processOrder(@PathVariable orderId: Long, request: ProcessOrderRequest): MessageResponse {
        val order = orderJpaRepository.findIncompleteOrder(orderId)
                .orElseThrow { EntityNotFoundException("Order not found - $orderId") }
        order.status = request.newStatus
        orderJpaRepository.save(order)
        return MessageResponse("Successfully processed order, new status - ${order.status}.")
    }
}