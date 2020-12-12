package coffee.store.payload.response.coffee

import coffee.store.model.CoffeeType

data class CoffeeListItemResponse(
        val id: Long?,
        val name: String?,
        val cost: Double?,
        val type: CoffeeType?,
        val avgRating: Double?,
        val smallPhoto: ByteArray?,
)