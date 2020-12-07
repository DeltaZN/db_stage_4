package coffee.store.payload.response

import coffee.store.payload.request.SubmitOrderItem
import java.time.DayOfWeek
import java.time.LocalTime

data class ScheduleFullItemComponent(
        val name: String?,
        val orderItems: List<SubmitOrderItem>,
        val dayOfWeek: DayOfWeek,
        val time: LocalTime,
)
