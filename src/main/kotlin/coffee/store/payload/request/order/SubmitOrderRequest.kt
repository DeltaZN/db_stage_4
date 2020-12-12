package coffee.store.payload.request.order

data class SubmitOrderRequest(
        val coffeeStoreId: Long,
        val orderItems: List<SubmitOrderItem>,
)