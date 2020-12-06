package coffee.store.controller

import coffee.store.entity.CoffeeScore
import coffee.store.payload.common.ScorePayload
import coffee.store.repository.CoffeeJpaRepository
import coffee.store.repository.CoffeeScoreJpaRepository
import coffee.store.repository.ScheduleScoreJpaRepository
import coffee.store.service.UserService
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.core.Authentication
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.web.bind.annotation.*
import javax.persistence.EntityNotFoundException

@CrossOrigin(origins = ["*"], maxAge = 3600)
@RestController
@RequestMapping("/api/scores")
class ScoresController(
        private val userService: UserService,
        private val coffeeJpaRepository: CoffeeJpaRepository,
        private val coffeeScoreJpaRepository: CoffeeScoreJpaRepository,
        private val scheduleScoreJpaRepository: ScheduleScoreJpaRepository,
) {
    @GetMapping("coffee/{coffeeId}")
    fun getCoffeeScores(@PathVariable coffeeId: Long): List<ScorePayload> =
            coffeeScoreJpaRepository.findScoresByCoffeeId(coffeeId)
                    .map { c -> ScorePayload(c.id, c.coffee.id, c.score, c.comment) }

    @RequestMapping(name = "coffee",
            method = [RequestMethod.POST, RequestMethod.PUT])
    @PreAuthorize("hasRole('CUSTOMER')")
    fun addCoffeeScore(auth: Authentication, payload: ScorePayload) {
        val user = userService.getUserFromAuth(auth)
        val coffeeScore = if (payload.id == null) CoffeeScore() else coffeeScoreJpaRepository.findById(payload.id)
                .orElseThrow { EntityNotFoundException("Coffee score not found - ${payload.id}") }
        payload.id?.let {
            if (coffeeScore.author.id != user.id)
                throw UsernameNotFoundException("An attempt to change a wrong score!")
        }
        coffeeScore.comment = payload.comment
        coffeeScore.score = payload.score
        val coffee = coffeeJpaRepository.findById(payload.itemId)
                .orElseThrow { EntityNotFoundException("Coffee not found = ${payload.itemId}") }
        if (payload.id == null) {
            if (coffeeScoreJpaRepository.findScoreByCoffeeIdAndUserId(coffee.id, user.id).isPresent)
                throw UsernameNotFoundException("An attempt to change a wrong score!")
            coffeeScore.coffee = coffee
        } else {
            if (!coffeeScoreJpaRepository.findScoreByCoffeeIdAndUserId(coffee.id, user.id).isPresent)
                throw UsernameNotFoundException("An attempt to change a wrong score!")
        }
        coffeeScoreJpaRepository.save(coffeeScore)
    }

    @GetMapping("schedule/{scheduleId}")
    fun getScheduleScores(@PathVariable scheduleId: Long): List<ScorePayload> =
            scheduleScoreJpaRepository.findScoresByScheduleId(scheduleId)
                    .map { c -> ScorePayload(c.id, c.schedule.id, c.score, c.comment) }

}