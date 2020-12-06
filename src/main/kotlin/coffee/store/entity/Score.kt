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
        open var score: Int = 0,
        @Column(name = "отзыв")
        open var comment: String? = null,
        @ManyToOne
        @JoinColumn(name = "id_клиента")
        open val author: User = User()
)