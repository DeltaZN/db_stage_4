package coffee.store.entity

import javax.persistence.*

@Entity
@Table(name = "оценка_кофе")
@PrimaryKeyJoinColumn(name = "id_оценки")
class CoffeeScore(
        id: Long = 0,
        score: Int = 0,
        comment: String? = null,
        @ManyToOne
        @JoinColumn(name = "id_кофе")
        val coffee: Coffee? = null,
) : Score(id, score, comment)