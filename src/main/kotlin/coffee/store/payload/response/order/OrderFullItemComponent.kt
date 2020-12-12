package coffee.store.payload.response.order

import coffee.store.model.ProductType

data class OrderFullItemComponent(
        val productId: Long,
        val name: String,
        val cost: Double,
        val quantity: Int,
        val type: ProductType,
)
