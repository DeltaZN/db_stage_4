package coffee.store.entity

import javax.persistence.*

@Entity
@Table(name = "оценка_расписания")
@PrimaryKeyJoinColumn(name = "id_оценки")
class ScheduleScore(
        id: Long = 0,
        score: Int = 0,
        comment: String? = null,
        @ManyToOne
        @JoinColumn(name = "id_расписания")
        val schedule: Schedule = Schedule(),
) : Score(id, score, comment)