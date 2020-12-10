package coffee.store.service

import coffee.store.payload.common.ScorePayload
import coffee.store.repository.CoffeeJpaRepository
import coffee.store.repository.CoffeeScoreJpaRepository
import coffee.store.repository.ScheduleJpaRepository
import coffee.store.repository.ScheduleScoreJpaRepository
import org.springframework.stereotype.Service
import javax.persistence.EntityNotFoundException

@Service
class ScoresService(
        private val userService: UserService,
        private val scheduleScoreJpaRepository: ScheduleScoreJpaRepository,
        private val scheduleJpaRepository: ScheduleJpaRepository,
        private val coffeeScoreJpaRepository: CoffeeScoreJpaRepository,
        private val coffeeJpaRepository: CoffeeJpaRepository,
) {

    fun addCoffeeScore(payload: ScorePayload) {
        val user = userService.getUserFromAuth()
        val coffee = coffeeJpaRepository.findById(payload.itemId)
                .orElseThrow { EntityNotFoundException("Coffee not found = ${payload.itemId}") }
        if (coffeeScoreJpaRepository.findScoreByCoffeeIdAndUserId(coffee.id, user.id).isPresent)
            throw IllegalAccessException("You can submit only one score per coffee")
        coffeeScoreJpaRepository.storeScore(payload.score, payload.comment, coffee.id, user.id)
    }

    fun editCoffeeScore(payload: ScorePayload) {
        val user = userService.getUserFromAuth()
        val coffeeScore = coffeeScoreJpaRepository.findById(payload.id!!)
                .orElseThrow { EntityNotFoundException("Coffee score not found - ${payload.id}") }
        userService.checkAuthority(coffeeScore, user)
        coffeeScore.comment = payload.comment
        coffeeScore.score = payload.score
        coffeeScoreJpaRepository.save(coffeeScore)
    }

    fun deleteCoffeeScore(id: Long) {
        val user = userService.getUserFromAuth()
        val coffeeScore = coffeeScoreJpaRepository.findById(id)
                .orElseThrow { EntityNotFoundException("Coffee score not found - $id") }
        userService.checkAuthority(coffeeScore, user)
        coffeeScoreJpaRepository.delete(coffeeScore)
    }

    fun addScheduleScore(payload: ScorePayload) {
        val user = userService.getUserFromAuth()
        val schedule = scheduleJpaRepository.findById(payload.itemId)
                .orElseThrow { EntityNotFoundException("Schedule not found = ${payload.itemId}") }
        if (scheduleScoreJpaRepository.findScoreByScheduleIdAndUserId(schedule.id, user.id).isPresent)
            throw IllegalAccessException("You can submit only one score per schedule")
        scheduleScoreJpaRepository.storeScore(payload.score, payload.comment, schedule.id, user.id)
    }

    fun editScheduleScore(payload: ScorePayload) {
        val user = userService.getUserFromAuth()
        val scheduleScore = scheduleScoreJpaRepository.findById(payload.id!!)
                .orElseThrow { EntityNotFoundException("Schedule score not found - ${payload.id}") }
        userService.checkAuthority(scheduleScore, user)
        scheduleScore.comment = payload.comment
        scheduleScore.score = payload.score
        scheduleScoreJpaRepository.save(scheduleScore)
    }

    fun deleteScheduleScore(id: Long) {
        val user = userService.getUserFromAuth()
        val scheduleScore = scheduleScoreJpaRepository.findById(id)
                .orElseThrow { EntityNotFoundException("Schedule score not found - $id") }
        userService.checkAuthority(scheduleScore, user)
        scheduleScoreJpaRepository.delete(scheduleScore)
    }
}
