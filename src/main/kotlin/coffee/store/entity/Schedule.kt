package coffee.store.entity

import javax.persistence.*

@Entity
@Table(name = "расписание")
data class Schedule(
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        val id: Long = 0,
        @Column(name = "название")
        val name: String = "",
        @ManyToOne
        @JoinColumn(name = "id_клиента")
        val author: User? = null,
        @Column(name = "описание")
        val description: String? = null,
        @Column(name = "состояние")
        val status: String = "",
)