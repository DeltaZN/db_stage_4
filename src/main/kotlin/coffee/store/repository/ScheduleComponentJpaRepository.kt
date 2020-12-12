package coffee.store.repository

import coffee.store.entity.Schedule
import coffee.store.entity.ScheduleComponent
import org.springframework.data.repository.CrudRepository

interface ScheduleComponentJpaRepository : CrudRepository<ScheduleComponent, Long> {
    fun findBySchedule(schedule: Schedule): Iterable<ScheduleComponent>
}