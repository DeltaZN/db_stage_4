package coffee.store.payload.request

data class SubmitOrderRequest(
        val coffeeStoreId: Long,
        val orderItems: List<SubmitOrderItem>,
)