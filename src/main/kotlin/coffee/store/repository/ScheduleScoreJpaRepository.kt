package coffee.store.repository

import coffee.store.entity.ScheduleScore
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import java.util.*

interface ScheduleScoreJpaRepository : CrudRepository<ScheduleScore, Long> {
    @Query(
            value = "select * from оценка_расписания join оценка on оценка_расписания.id_оценки = оценка.id where id_расписания = ?",
            countQuery = "select count(*) from оценка_расписания where id_расписания = ?",
            nativeQuery = true)
    fun findScoresByScheduleId(scheduleId: Long): Iterable<ScheduleScore>

    @Query(
            value = "select * from оценка_расписания join оценка on оценка_расписания.id_оценки = оценка.id where id_расписания = ? and id_клиента = ?",
            countQuery = "select count(*) from оценка_расписания join оценка on оценка_расписания.id_оценки = оценка.id where id_расписания = ? and id_клиента = ?",
            nativeQuery = true)
    fun findScoreByScheduleIdAndUserId(scheduleId: Long, clientId: Long): Optional<ScheduleScore>

    @Query(
            value = "select avg(оценка.оценка) from оценка_расписания join оценка on оценка_расписания.id_оценки = оценка.id where id_расписания = ?",
            countQuery = "select 1",
            nativeQuery = true)
    fun getAverageScoreBySchedule(scheduleId: Long): Double?

    @Query(
            value = "select addScheduleRating(?, ?, ?, ?)",
            countQuery = "select 1",
            nativeQuery = true)
    fun storeScore(rating: Int, comment: String?, scheduleId: Long, clientId: Long): Long
}