package coffee.store.payload.response

import coffee.store.model.CoffeeType

data class CoffeeListItemResponse(
        val id: Long,
        val name: String,
        val cost: Double,
        val type: CoffeeType,
        val avgRating: Double? = null,
        val photo: ByteArray? = null,
)