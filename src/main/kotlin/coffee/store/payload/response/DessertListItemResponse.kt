package coffee.store.payload.response

class DessertListItemResponse(
        val id: Long,
        val name: String,
        val cost: Double,
        val smallPhoto: ByteArray? = null,
)