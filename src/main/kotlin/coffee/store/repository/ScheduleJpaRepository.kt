package coffee.store.repository

import coffee.store.entity.Schedule
import coffee.store.repository.projection.ScheduleListItemProjection
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository

interface ScheduleJpaRepository : CrudRepository<Schedule, Long> {
    @Query(
            value = "select расписание.id, расписание.название as name, расписание.состояние as status, расписание.описание as description, avg(о.оценка) as avgRating from расписание join оценка_расписания ор on расписание.id = ор.id_расписания join оценка о on ор.id_оценки = о.id where расписание.id_клиента = ? group by расписание.id, расписание.название, расписание.состояние, расписание.описание",
            countQuery = "select count(*) from расписание where id_клиента = ?",
            nativeQuery = true)
    fun findUserSchedules(userId: Long): Iterable<ScheduleListItemProjection>

    @Query(
            value = "select расписание.id, расписание.название as name, расписание.состояние as status, расписание.описание as description, avg(о.оценка) as avgRating from расписание join оценка_расписания ор on расписание.id = ор.id_расписания join оценка о on ор.id_оценки = о.id where состояние = 'PUBLISHED' group by расписание.id, расписание.название, расписание.состояние, расписание.описание",
            countQuery = "select count(*) from расписание where состояние = 'PUBLISHED'",
            nativeQuery = true)
    fun findAllPublicSchedules(): Iterable<ScheduleListItemProjection>
}