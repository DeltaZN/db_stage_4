package coffee.store.entity

import java.time.DayOfWeek
import java.time.LocalTime
import javax.persistence.*

@Entity
@Table(name = "запись_расписания")
class ScheduleComponent(
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        val id: Long = 0,
        @Column(name = "название")
        val name: String = "",
        @ManyToOne
        @JoinColumn(name = "id_расписания")
        val schedule: Schedule? = null,
        @ManyToOne
        @JoinColumn(name = "id_заказа")
        val order: Order? = null,
        @Column(name = "день_недели")
        val dayOfWeek: DayOfWeek? = null,
        @Column(name = "время")
        val time: LocalTime? = null,
)