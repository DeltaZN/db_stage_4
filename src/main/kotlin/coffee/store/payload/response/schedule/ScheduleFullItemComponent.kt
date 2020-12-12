package coffee.store.payload.response.schedule

import coffee.store.payload.request.order.SubmitOrderItem
import java.time.DayOfWeek
import java.time.LocalTime

data class ScheduleFullItemComponent(
        val name: String?,
        val orderItems: List<SubmitOrderItem>,
        val dayOfWeek: DayOfWeek,
        val time: LocalTime,
)
