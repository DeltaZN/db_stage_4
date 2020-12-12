package coffee.store.payload.response.order

import coffee.store.model.OrderStatus
import java.time.LocalDateTime

data class OrderListItemResponse(
        val id: Long,
        val status: OrderStatus,
        val cost: Double?,
        val discount: Double?,
        val time: LocalDateTime?,
)