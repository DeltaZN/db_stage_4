package coffee.store.payload.request.schedule

import coffee.store.payload.request.SubmitOrderItem
import java.time.LocalTime

data class AddScheduleComponent(
        val name: String,
        val dayOfWeek: Int,
        val time: LocalTime,
        val orderItems: List<SubmitOrderItem>,
)
