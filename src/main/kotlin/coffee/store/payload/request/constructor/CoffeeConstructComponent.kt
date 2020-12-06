package coffee.store.payload.request.constructor

data class CoffeeConstructComponent(
        val ingredientId: Long,
        val addingOrder: Int,
        val quantity: Double,
        val volumeMl: Double,
)
