package coffee.store.payload.request

import coffee.store.model.OrderStatus

data class ProcessOrderRequest(
        val newStatus: OrderStatus,
)
