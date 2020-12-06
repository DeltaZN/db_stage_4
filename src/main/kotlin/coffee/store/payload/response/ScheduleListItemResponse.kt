package coffee.store.payload.response

import coffee.store.model.ScheduleStatus

data class ScheduleListItemResponse(
        val id: Long,
        val name: String,
        val description: String?,
        val status: ScheduleStatus,
)
