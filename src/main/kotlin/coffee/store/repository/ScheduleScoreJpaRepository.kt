package coffee.store.repository

import coffee.store.entity.ScheduleScore
import org.springframework.data.repository.CrudRepository

interface ScheduleScoreJpaRepository : CrudRepository<ScheduleScore, Long>