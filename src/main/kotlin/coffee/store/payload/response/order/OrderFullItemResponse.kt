package coffee.store.payload.response.order

import coffee.store.entity.CoffeeStore
import coffee.store.model.OrderStatus
import java.time.LocalDateTime

data class OrderFullItemResponse(
        val id: Long,
        val status: OrderStatus,
        val coffeeStore: CoffeeStore?,
        val discount: Double?,
        val cost: Double,
        val orderTime: LocalDateTime?,
        var items: List<OrderFullItemComponent>,
)