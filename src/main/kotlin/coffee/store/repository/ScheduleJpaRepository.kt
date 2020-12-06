package coffee.store.repository

import coffee.store.entity.Schedule
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository

interface ScheduleJpaRepository : CrudRepository<Schedule, Long> {
    @Query(
            value = "select * from расписание where id_клиента = ?",
            countQuery = "select count(*) from расписание where id_клиента = ?",
            nativeQuery = true)
    fun findUserSchedules(userId: Long): Iterable<Schedule>
}