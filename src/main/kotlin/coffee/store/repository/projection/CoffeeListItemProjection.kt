package coffee.store.repository.projection

interface CoffeeListItemProjection {
    val id: Long?
    val name: String
    val cost: Double
    val type: String
    val avgRating: Double?
    val photo: ByteArray?
}