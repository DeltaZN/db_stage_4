package coffee.store.payload.response

import coffee.store.entity.CoffeeStore
import coffee.store.model.OrderStatus
import coffee.store.payload.request.SubmitOrderItem
import java.time.LocalDateTime

data class OrderFullItemResponse(
        val id: Long,
        val status: OrderStatus,
        val coffeeStore: CoffeeStore?,
        val discount: Double?,
        val cost: Double,
        val orderTime: LocalDateTime?,
        var items: List<SubmitOrderItem>,
)