package coffee.store.entity

import javax.persistence.*

@Entity
@Table(name = "оценка_расписания")
@PrimaryKeyJoinColumn(name = "id_оценки")
class ScheduleScore(
        id: Long = 0,
        score: Int = 0,
        comment: String? = null,
        author: User = User(),
        @ManyToOne
        @JoinColumn(name = "id_расписания")
        var schedule: Schedule = Schedule(),
) : Score(id, score, comment, author)