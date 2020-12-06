package coffee.store.payload.request

import coffee.store.model.ProductType

data class SubmitOrderItem(
        val productId: Long,
        val quantity: Int,
        val type: ProductType,
)