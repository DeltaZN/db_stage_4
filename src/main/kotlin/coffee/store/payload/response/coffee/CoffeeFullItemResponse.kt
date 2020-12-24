package coffee.store.payload.response.coffee

import coffee.store.model.CoffeeStatus
import coffee.store.model.CoffeeType

class CoffeeFullItemResponse(
        val id: Long,
        val name: String,
        val cost: Double,
        val type: CoffeeType,
        val author: String,
        val status: CoffeeStatus,
        val avgRating: Double? = null,
        val photo: String? = null,
        val components: List<CoffeeFullItemComponent>,
)