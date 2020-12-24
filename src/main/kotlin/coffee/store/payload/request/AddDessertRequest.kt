package coffee.store.payload.request

data class AddDessertRequest(
        val name: String,
        val cost: Double,
        val calories: Double,
        val weight: Double,
        val photo: String?,
)
