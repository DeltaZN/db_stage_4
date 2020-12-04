package coffee.store.entity

import javax.persistence.*

@Entity
@Table(name = "оценка")
@Inheritance(strategy = InheritanceType.JOINED)
abstract class Score(
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        open val id: Long = 0,
        @Column(name = "оценка")
        open val score: Int = 0,
        @Column(name = "отзыв")
        open val comment: String? = null,
)