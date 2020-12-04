package coffee.store.repository

import coffee.store.entity.Schedule
import org.springframework.data.repository.CrudRepository

interface ScheduleJpaRepository : CrudRepository<Schedule, Long>