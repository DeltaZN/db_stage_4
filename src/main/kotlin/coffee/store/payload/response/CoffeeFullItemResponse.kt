package coffee.store.payload.response

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
        val photo: ByteArray? = null,
        val components: List<CoffeeFullItemComponent>,
)