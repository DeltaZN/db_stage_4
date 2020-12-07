package coffee.store.repository.projection

interface ScheduleListItemProjection {
    val id: Long
    val name: String?
    val description: String?
    val avgRating: Double?
    val status: String
}