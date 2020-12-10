package coffee.store.controller

import coffee.store.model.ScheduleStatus
import coffee.store.payload.request.schedule.ScheduleRequest
import coffee.store.payload.response.MessageResponse
import coffee.store.payload.response.ScheduleListItemResponse
import coffee.store.repository.ScheduleJpaRepository
import coffee.store.service.ScheduleService
import coffee.store.service.UserService
import io.swagger.annotations.Api
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*

@CrossOrigin(origins = ["*"], maxAge = 3600)
@RestController
@RequestMapping("/api/schedules")
@Api(tags = ["Schedule"])
@PreAuthorize("hasRole('CUSTOMER')")
class ScheduleController(
        private val scheduleJpaRepository: ScheduleJpaRepository,
        private val scheduleService: ScheduleService,
        private val userService: UserService,
) {

    @GetMapping
    fun getSchedules(): List<ScheduleListItemResponse> =
            scheduleJpaRepository.findUserSchedules(userService.getCurrentUserId())
                    .map { s -> ScheduleListItemResponse(s.id, s.name, s.description, s.avgRating, ScheduleStatus.valueOf(s.status)) }

    @PostMapping
    fun addSchedule(@RequestBody payload: ScheduleRequest): MessageResponse {
        scheduleService.addSchedule(payload)
        return MessageResponse("Schedule added!")
    }

    @PostMapping("copy_schedule/{id}")
    fun copySchedule(@PathVariable id: Long) = scheduleService.copyAndGetSchedule(id)

    @PutMapping
    fun editSchedule(@RequestBody payload: ScheduleRequest): MessageResponse {
        scheduleService.editCustomSchedule(payload)
        return MessageResponse("Schedule edited!")
    }
}