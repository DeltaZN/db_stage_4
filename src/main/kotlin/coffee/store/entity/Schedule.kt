package coffee.store.entity

import coffee.store.model.ScheduleStatus
import javax.persistence.*

@Entity
@Table(name = "расписание")
data class Schedule(
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        val id: Long = 0,
        @Column(name = "название")
        var name: String = "",
        @ManyToOne
        @JoinColumn(name = "id_клиента")
        var author: User? = null,
        @Column(name = "описание")
        var description: String? = null,
        @Enumerated(EnumType.STRING)
        @Column(name = "состояние")
        var status: ScheduleStatus = ScheduleStatus.HIDDEN,
        @OneToMany(fetch = FetchType.LAZY, mappedBy = "schedule")
        var components: List<ScheduleComponent> = mutableListOf(),
)