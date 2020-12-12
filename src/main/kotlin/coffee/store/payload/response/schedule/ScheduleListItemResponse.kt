package coffee.store.payload.response.schedule

import coffee.store.model.ScheduleStatus

data class ScheduleListItemResponse(
        val id: Long,
        val name: String?,
        val description: String?,
        val avgRating: Double?,
        val status: ScheduleStatus,
)
