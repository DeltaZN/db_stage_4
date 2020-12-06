package coffee.store.controller

import coffee.store.auth.UserDetailsImpl
import coffee.store.entity.Order
import coffee.store.entity.OrderItem
import coffee.store.entity.Schedule
import coffee.store.entity.ScheduleComponent
import coffee.store.model.OrderStatus
import coffee.store.model.ProductType
import coffee.store.payload.request.schedule.AddScheduleRequest
import coffee.store.payload.response.MessageResponse
import coffee.store.payload.response.ScheduleListItemResponse
import coffee.store.repository.CoffeeJpaRepository
import coffee.store.repository.DessertJpaRepository
import coffee.store.repository.ScheduleJpaRepository
import coffee.store.repository.UserJpaRepository
import coffee.store.service.UserService
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.core.Authentication
import org.springframework.security.core.userdetails.UsernameNotFoundException
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
                    .map { s -> ScheduleListItemResponse(s.id, s.name, s.description, s.status) }

    @PostMapping
    @PutMapping
    fun addCustomSchedule(auth: Authentication, payload: AddScheduleRequest): MessageResponse {
        val user = userService.getUserFromAuth(auth)
        val schedule = if (payload.id == null) Schedule() else scheduleJpaRepository.findById(payload.id)
                .orElseThrow { EntityNotFoundException("Schedule not found - ${payload.id}") }
        payload.id?.let {
            if (schedule.author?.id != user.id)
                throw UsernameNotFoundException("An access try to wrong schedule!")
        }
        schedule.name = schedule.name
        schedule.description = payload.description
        schedule.author = user
        schedule.status = payload.status
        schedule.components = payload.scheduleComponents.asIterable()
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
        scheduleJpaRepository.save(schedule)
        return MessageResponse("Schedule submitted!")
    }
}