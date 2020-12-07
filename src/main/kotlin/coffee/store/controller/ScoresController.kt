package coffee.store.controller

import coffee.store.entity.CoffeeScore
import coffee.store.entity.ScheduleScore
import coffee.store.payload.common.ScorePayload
import coffee.store.payload.response.MessageResponse
import coffee.store.repository.CoffeeJpaRepository
import coffee.store.repository.CoffeeScoreJpaRepository
import coffee.store.repository.ScheduleJpaRepository
import coffee.store.repository.ScheduleScoreJpaRepository
import coffee.store.service.UserService
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.*
import javax.persistence.EntityNotFoundException

@CrossOrigin(origins = ["*"], maxAge = 3600)
@RestController
@RequestMapping("/api/scores")
class ScoresController(
        private val userService: UserService,
        private val coffeeJpaRepository: CoffeeJpaRepository,
        private val coffeeScoreJpaRepository: CoffeeScoreJpaRepository,
        private val scheduleJpaRepository: ScheduleJpaRepository,
        private val scheduleScoreJpaRepository: ScheduleScoreJpaRepository,
) {
    @GetMapping("schedule/{scheduleId}")
    fun getScheduleScores(@PathVariable scheduleId: Long): List<ScorePayload> =
            scheduleScoreJpaRepository.findScoresByScheduleId(scheduleId)
                    .map { c -> ScorePayload(c.id, c.schedule.id, c.score, c.comment) }

    @GetMapping("coffee/{coffeeId}")
    fun getCoffeeScores(@PathVariable coffeeId: Long): List<ScorePayload> =
            coffeeScoreJpaRepository.findScoresByCoffeeId(coffeeId)
                    .map { c -> ScorePayload(c.id, c.coffee.id, c.score, c.comment) }

    @PostMapping("coffee")
    @PreAuthorize("hasRole('CUSTOMER')")
    fun addCoffeeScore(auth: Authentication, payload: ScorePayload): MessageResponse {
        val user = userService.getUserFromAuth(auth)
        val coffeeScore = CoffeeScore()
        val coffee = coffeeJpaRepository.findById(payload.itemId)
                .orElseThrow { EntityNotFoundException("Coffee not found = ${payload.itemId}") }
        if (coffeeScoreJpaRepository.findScoreByCoffeeIdAndUserId(coffee.id, user.id).isPresent)
            throw IllegalAccessException("You can submit only one score per coffee")
        coffeeScore.comment = payload.comment
        coffeeScore.score = payload.score
        coffeeScore.coffee = coffee
        coffeeScoreJpaRepository.save(coffeeScore)
        return MessageResponse("Score successfully submitted!")
    }

    @PutMapping("coffee")
    @PreAuthorize("hasRole('CUSTOMER')")
    fun editCoffeeScore(auth: Authentication, payload: ScorePayload): MessageResponse {
        val user = userService.getUserFromAuth(auth)
        val coffeeScore = coffeeScoreJpaRepository.findById(payload.id!!)
                .orElseThrow { EntityNotFoundException("Coffee score not found - ${payload.id}") }
        if (coffeeScore.author.id != user.id)
            throw IllegalAccessException("An attempt to change a wrong score!")
        coffeeScore.comment = payload.comment
        coffeeScore.score = payload.score
        coffeeScoreJpaRepository.save(coffeeScore)
        return MessageResponse("Coffee successfully edited!")
    }

    @DeleteMapping("coffee/{id}")
    @PreAuthorize("hasRole('CUSTOMER')")
    fun deleteCoffeeScore(auth: Authentication, @PathVariable id: Long): MessageResponse {
        val user = userService.getUserFromAuth(auth)
        val coffeeScore = coffeeScoreJpaRepository.findById(id)
                .orElseThrow { EntityNotFoundException("Coffee score not found - $id") }
        if (coffeeScore.author.id != user.id)
            throw IllegalAccessException("An attempt to delete a wrong score!")
        coffeeScoreJpaRepository.delete(coffeeScore)
        return MessageResponse("Coffee successfully deleted!")
    }

    @PostMapping("schedule")
    @PreAuthorize("hasRole('CUSTOMER')")
    fun addScheduleScore(auth: Authentication, payload: ScorePayload): MessageResponse {
        val user = userService.getUserFromAuth(auth)
        val scheduleScore = ScheduleScore()
        scheduleScore.comment = payload.comment
        scheduleScore.score = payload.score
        val schedule = scheduleJpaRepository.findById(payload.itemId)
                .orElseThrow { EntityNotFoundException("Schedule not found = ${payload.itemId}") }
        if (payload.id == null) {
            if (scheduleScoreJpaRepository.findScoreByScheduleIdAndUserId(schedule.id, user.id).isPresent)
                throw IllegalAccessException("You can submit only one score per schedule")
            scheduleScore.schedule = schedule
        }
        scheduleScoreJpaRepository.save(scheduleScore)
        return MessageResponse("Score successfully submitted!")
    }

    @PutMapping("schedule")
    @PreAuthorize("hasRole('CUSTOMER')")
    fun editScheduleScore(auth: Authentication, payload: ScorePayload): MessageResponse {
        val user = userService.getUserFromAuth(auth)
        val scheduleScore = scheduleScoreJpaRepository.findById(payload.id!!)
                .orElseThrow { EntityNotFoundException("Schedule score not found - ${payload.id}") }
        if (scheduleScore.author.id != user.id)
            throw IllegalAccessException("An attempt to change a wrong score!")
        scheduleScore.comment = payload.comment
        scheduleScore.score = payload.score
        scheduleScoreJpaRepository.save(scheduleScore)
        return MessageResponse("Score successfully edited!")
    }

    @DeleteMapping("schedule/{id}")
    @PreAuthorize("hasRole('CUSTOMER')")
    fun deleteScheduleScore(auth: Authentication, @PathVariable id: Long): MessageResponse {
        val user = userService.getUserFromAuth(auth)
        val scheduleScore = scheduleScoreJpaRepository.findById(id)
                .orElseThrow { EntityNotFoundException("Schedule score not found - $id") }
        if (scheduleScore.author.id != user.id)
            throw IllegalAccessException("An attempt to delete a wrong score!")
        scheduleScoreJpaRepository.delete(scheduleScore)
        return MessageResponse("Score successfully deleted!")
    }
}