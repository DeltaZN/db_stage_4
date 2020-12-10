package coffee.store.controller

import coffee.store.payload.common.ScorePayload
import coffee.store.payload.response.MessageResponse
import coffee.store.repository.CoffeeScoreJpaRepository
import coffee.store.repository.ScheduleScoreJpaRepository
import coffee.store.service.ScoresService
import io.swagger.annotations.Api
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*

@CrossOrigin(origins = ["*"], maxAge = 3600)
@RestController
@RequestMapping("/api/scores")
@Api(tags = ["Score"])
class ScoresController(
        private val coffeeScoreJpaRepository: CoffeeScoreJpaRepository,
        private val scheduleScoreJpaRepository: ScheduleScoreJpaRepository,
        private val scoresService: ScoresService,
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
    fun addCoffeeScore(@RequestBody payload: ScorePayload): MessageResponse {
        scoresService.addCoffeeScore(payload)
        return MessageResponse("Score successfully submitted!")
    }

    @PutMapping("coffee")
    @PreAuthorize("hasRole('CUSTOMER')")
    fun editCoffeeScore(@RequestBody payload: ScorePayload): MessageResponse {
        scoresService.editCoffeeScore(payload)
        return MessageResponse("Coffee successfully edited!")
    }

    @DeleteMapping("coffee/{id}")
    @PreAuthorize("hasRole('CUSTOMER')")
    fun deleteCoffeeScore(@PathVariable id: Long): MessageResponse {
        scoresService.deleteCoffeeScore(id)
        return MessageResponse("Coffee successfully deleted!")
    }

    @PostMapping("schedule")
    @PreAuthorize("hasRole('CUSTOMER')")
    fun addScheduleScore(@RequestBody payload: ScorePayload): MessageResponse {
        scoresService.addScheduleScore(payload)
        return MessageResponse("Score successfully submitted!")
    }

    @PutMapping("schedule")
    @PreAuthorize("hasRole('CUSTOMER')")
    fun editScheduleScore(@RequestBody payload: ScorePayload): MessageResponse {
        scoresService.editScheduleScore(payload)
        return MessageResponse("Score successfully edited!")
    }

    @DeleteMapping("schedule/{id}")
    @PreAuthorize("hasRole('CUSTOMER')")
    fun deleteScheduleScore(@PathVariable id: Long): MessageResponse {
        scoresService.deleteScheduleScore(id)
        return MessageResponse("Score successfully deleted!")
    }
}