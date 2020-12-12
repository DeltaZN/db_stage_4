package coffee.store.controller

import coffee.store.entity.Coffee
import coffee.store.entity.Dessert
import coffee.store.model.CoffeeType
import coffee.store.model.ProductType
import coffee.store.payload.request.AddDessertRequest
import coffee.store.payload.request.ProcessOrderRequest
import coffee.store.payload.request.constructor.CoffeeConstructRequest
import coffee.store.payload.response.MessageResponse
import coffee.store.payload.response.order.OrderFullItemComponent
import coffee.store.payload.response.order.OrderFullItemResponse
import coffee.store.payload.response.order.OrderListItemResponse
import coffee.store.repository.DessertJpaRepository
import coffee.store.repository.OrderJpaRepository
import coffee.store.service.CoffeeService
import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*
import javax.persistence.EntityNotFoundException
import javax.transaction.Transactional

@CrossOrigin(origins = ["*"], maxAge = 3600)
@RestController
@RequestMapping("/api/barista")
@Api(tags = ["Barista"])
@PreAuthorize("hasRole('BARISTA')")
class BaristaController(
        private val orderJpaRepository: OrderJpaRepository,
        private val dessertJpaRepository: DessertJpaRepository,
        private val coffeeService: CoffeeService,
) {
    @GetMapping("stores/{coffeeStoreId}/orders")
    fun getIncompleteOrdersByCoffeeStore(@PathVariable coffeeStoreId: Long): List<OrderListItemResponse> =
            orderJpaRepository.findIncompleteOrders(coffeeStoreId)
                    .map { o -> OrderListItemResponse(o.id, o.status, o.cost, o.discount, o.orderTime) }

    @GetMapping("orders/{orderId}")
    @Transactional
    fun getIncompleteOrder(@PathVariable orderId: Long): OrderFullItemResponse {
        val order = orderJpaRepository.findIncompleteOrder(orderId)
                .orElseThrow { EntityNotFoundException("Incomplete order not found - $orderId") }
        return OrderFullItemResponse(order.id, order.status, order.coffeeStore, order.discount,
                order.cost, order.orderTime, order.items.map
        { i ->
            OrderFullItemComponent(i.product.id, i.product.name, i.product.cost, i.quantity,
                    if (i.product is Coffee) ProductType.Coffee else ProductType.Dessert)
        })
    }


    @PostMapping("orders/{orderId}")
    fun processOrder(@PathVariable orderId: Long, request: ProcessOrderRequest): MessageResponse {
        val order = orderJpaRepository.findIncompleteOrder(orderId)
                .orElseThrow { EntityNotFoundException("Order not found - $orderId") }
        order.status = request.newStatus
        orderJpaRepository.save(order)
        return MessageResponse("Successfully processed order, new status - ${order.status}.")
    }

    @PostMapping("dessert")
    fun createDessert(payload: AddDessertRequest): MessageResponse {
        val dessert = Dessert(0, payload.calories, payload.weight, payload.name, payload.cost, payload.photo)
        dessertJpaRepository.save(dessert)
        return MessageResponse("Successfully create dessert, id - ${dessert.id}")
    }

    @ApiOperation("Add standart coffee as barista")
    @PostMapping("coffee")
    fun addStandardCoffee(@RequestBody payload: CoffeeConstructRequest): MessageResponse {
        coffeeService.addCoffee(payload, CoffeeType.s)
        return MessageResponse("Successfully added standart coffee!")
    }
}