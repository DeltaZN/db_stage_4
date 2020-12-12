package coffee.store.service

import coffee.store.entity.*
import coffee.store.model.OrderStatus
import coffee.store.model.ProductType
import coffee.store.payload.request.SubmitOrderItem
import coffee.store.payload.request.schedule.AddScheduleComponent
import coffee.store.payload.request.schedule.ScheduleRequest
import coffee.store.payload.response.ScheduleFullItemComponent
import coffee.store.payload.response.ScheduleFullItemResponse
import coffee.store.repository.CoffeeJpaRepository
import coffee.store.repository.DessertJpaRepository
import coffee.store.repository.ScheduleJpaRepository
import coffee.store.repository.ScheduleScoreJpaRepository
import org.springframework.stereotype.Service
import java.time.DayOfWeek
import javax.persistence.EntityNotFoundException
import javax.transaction.Transactional

@Service
class ScheduleService(
        private val scheduleJpaRepository: ScheduleJpaRepository,
        private val scheduleScoreJpaRepository: ScheduleScoreJpaRepository,
        private val coffeeJpaRepository: CoffeeJpaRepository,
        private val dessertJpaRepository: DessertJpaRepository,
        private val userService: UserService,
) {
    @Transactional
    fun getSchedule(id: Long): ScheduleFullItemResponse {
        val schedule = scheduleJpaRepository.findById(id).orElseThrow { EntityNotFoundException("Schedule not found - $id") }
        val components = schedule.components.asIterable()
                .map { c ->
                    val items = c.order.items.asIterable().map { i ->
                        SubmitOrderItem(i.product.id, i.quantity, if (i.product is Coffee) ProductType.Coffee else ProductType.Dessert)
                    }
                    ScheduleFullItemComponent(c.name, items, c.dayOfWeek, c.time)
                }
        return ScheduleFullItemResponse(schedule.id, schedule.name,
                "${schedule.author.firstName} ${schedule.author.lastName}",
                scheduleScoreJpaRepository.getAverageScoreBySchedule(schedule.id),
                schedule.description, schedule.status, components)
    }

    fun addSchedule(payload: ScheduleRequest) {
        val user = userService.getUserFromAuth()
        val schedule = Schedule()
        schedule.name = payload.name!!
        schedule.description = payload.description
        schedule.author = user
        schedule.status = payload.status!!
        payload.scheduleComponents?.let {
            schedule.components = transformScheduleComponents(it, schedule, user)
        }
        scheduleJpaRepository.save(schedule)
    }

    fun editCustomSchedule(payload: ScheduleRequest) {
        val user = userService.getUserFromAuth()
        val schedule = scheduleJpaRepository.findById(payload.id!!)
                .orElseThrow { EntityNotFoundException("Schedule not found - ${payload.id}") }
        userService.checkAuthority(schedule)
        payload.name?.let { schedule.name = it }
        payload.description?.let { schedule.description = it }
        payload.status?.let { schedule.status = it }
        payload.scheduleComponents?.let {
            schedule.components = transformScheduleComponents(it, schedule, user)
        }
        scheduleJpaRepository.save(schedule)
    }

    fun copyAndGetSchedule(id: Long): ScheduleFullItemResponse {
        val user = userService.getCurrentUserId()
        val newId = scheduleJpaRepository.copySchedule(id, user)
        return getSchedule(newId)
    }

    private fun transformScheduleComponents(components: List<AddScheduleComponent>, schedule: Schedule, user: User) = components.asIterable()
            .map { s ->
                val scheduleOrder = Order(0, OrderStatus.TEMPLATE, user, null, 0.0,
                        0.0, null)
                scheduleOrder.items = s.orderItems
                        .map { i ->
                            val product = if (i.type === ProductType.Coffee)
                                coffeeJpaRepository.findById(i.productId)
                            else
                                dessertJpaRepository.findById(i.productId)
                            OrderItem(0, scheduleOrder, product
                                    .orElseThrow { EntityNotFoundException("Product not found - ${i.productId}") }, i.quantity)
                        }
                ScheduleComponent(0, s.name, schedule, scheduleOrder, DayOfWeek.of(s.dayOfWeek), s.time)
            }

}