package coffee.store.payload.response

data class CoffeeFullItemComponent(
        val name: String,
        val addingOrder: Int,
        val quantity: Double,
        val volumeMl: Double,
)
