package coffee.store.controller

import coffee.store.auth.UserDetailsImpl
import coffee.store.entity.Order
import coffee.store.entity.OrderItem
import coffee.store.entity.Schedule
import coffee.store.entity.ScheduleComponent
import coffee.store.model.OrderStatus
import coffee.store.model.ProductType
import coffee.store.model.ScheduleStatus
import coffee.store.payload.request.schedule.ScheduleRequest
import coffee.store.payload.response.MessageResponse
import coffee.store.payload.response.ScheduleListItemResponse
import coffee.store.repository.CoffeeJpaRepository
import coffee.store.repository.DessertJpaRepository
import coffee.store.repository.ScheduleJpaRepository
import coffee.store.service.UserService
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.*
import java.time.DayOfWeek
import javax.persistence.EntityNotFoundException

@CrossOrigin(origins = ["*"], maxAge = 3600)
@RestController
@RequestMapping("/api/schedules")
@PreAuthorize("hasRole('CUSTOMER')")
class ScheduleController(
        private val coffeeJpaRepository: CoffeeJpaRepository,
        private val dessertJpaRepository: DessertJpaRepository,
        private val scheduleJpaRepository: ScheduleJpaRepository,
        private val userService: UserService,
) {

    @GetMapping
    fun getCustomSchedules(auth: Authentication): List<ScheduleListItemResponse> =
            scheduleJpaRepository.findUserSchedules((auth.principal as UserDetailsImpl).id)
                    .map { s -> ScheduleListItemResponse(s.id, s.name, s.description, s.avgRating, ScheduleStatus.valueOf(s.status)) }

    @PostMapping
    fun addCustomSchedule(auth: Authentication, payload: ScheduleRequest): MessageResponse {
        val user = userService.getUserFromAuth(auth)
        val schedule = Schedule()
        schedule.name = payload.name!!
        schedule.description = payload.description
        schedule.author = user
        schedule.status = payload.status!!
        payload.scheduleComponents?.let {
            schedule.components = it.asIterable().map { s ->
                val scheduleOrder = Order(0, OrderStatus.TEMPLATE, user, null, 0.0,
                        0.0, null)
                scheduleOrder.items = s.orderItems
                        .map { i ->
                            val product = if (i.type === ProductType.Coffee)
                                coffeeJpaRepository.findById(i.productId)
                            else
                                dessertJpaRepository.findById(i.productId)
                            OrderItem(0, scheduleOrder, product
                                    .orElseThrow { EntityNotFoundException("${i.type} not found - ${i.productId}") }, i.quantity)
                        }
                ScheduleComponent(0, s.name, schedule, scheduleOrder, DayOfWeek.of(s.dayOfWeek), s.time)
            }
        }
        scheduleJpaRepository.save(schedule)
        return MessageResponse("Schedule added!")
    }

    @PutMapping
    fun editCustomSchedule(auth: Authentication, payload: ScheduleRequest): MessageResponse {
        val user = userService.getUserFromAuth(auth)
        val schedule = scheduleJpaRepository.findById(payload.id!!)
                .orElseThrow { EntityNotFoundException("Schedule not found - ${payload.id}") }
        if (schedule.author.id != user.id)
            throw IllegalAccessException("An access try to wrong schedule!")
        payload.name?.let { schedule.name = it }
        payload.description?.let { schedule.description = it }
        payload.status?.let { schedule.status = it }
        payload.scheduleComponents?.let {
            schedule.components = it.asIterable()
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
        scheduleJpaRepository.save(schedule)
        return MessageResponse("Schedule edited!")
    }
}