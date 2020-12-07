package coffee.store.payload.response

import coffee.store.model.ScheduleStatus

data class ScheduleFullItemResponse(
        val id: Long,
        val name: String?,
        val author: String,
        val avgRating: Double?,
        val description: String?,
        var status: ScheduleStatus,
        val schedule: List<ScheduleFullItemComponent>,
)
