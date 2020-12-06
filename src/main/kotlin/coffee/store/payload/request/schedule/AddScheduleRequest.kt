package coffee.store.payload.request.schedule

import coffee.store.model.ScheduleStatus

data class AddScheduleRequest(
        val id: Long?,
        val name: String,
        val description: String?,
        val status: ScheduleStatus,
        val scheduleComponents: List<AddScheduleComponent>,
)
