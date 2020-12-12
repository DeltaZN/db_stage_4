package coffee.store.payload.request.schedule

import coffee.store.payload.request.order.SubmitOrderItem
import java.time.LocalTime

data class AddScheduleComponent(
        val name: String,
        val dayOfWeek: Int,
        val time: LocalTime,
        val orderItems: List<SubmitOrderItem>,
)
