package coffee.store.entity

import coffee.store.model.Sex
import java.time.LocalDate
import javax.persistence.*

@Entity
@Table(name = "клиент")
data class User(
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        var id: Long = 0,
        @Column(name = "имя")
        var firstName: String = "",
        @Column(name = "фамилия")
        var lastName: String = "",
        @Column(name = "пол")
        @Enumerated(EnumType.STRING)
        var sex: Sex = Sex.M,
        @Column(name = "дата_рождения")
        var birthDay: LocalDate? = null,
        @ManyToOne
        @JoinColumn(name = "id_адреса")
        var address: Address? = null,
        @Column(name = "email")
        var email: String? = null,
        @Column(name = "телефон")
        var phone: String = "",
        @Column(name = "пароль")
        var password: String = "",
        @ManyToMany(fetch = FetchType.EAGER)
        @JoinTable(name = "роли_пользователей",
                joinColumns = [JoinColumn(name = "id_клиента")],
                inverseJoinColumns = [JoinColumn(name = "id_роли")])
        val roles: Set<Role> = emptySet(),
        @ManyToMany(fetch = FetchType.LAZY)
        @JoinTable(name = "любимые_кофе",
                joinColumns = [JoinColumn(name = "id_клиента")],
                inverseJoinColumns = [JoinColumn(name = "id_кофе")])
        var favoriteCoffees: Set<Coffee> = emptySet(),
        @ManyToMany(fetch = FetchType.LAZY)
        @JoinTable(name = "любимые_расписания",
                joinColumns = [JoinColumn(name = "id_клиента")],
                inverseJoinColumns = [JoinColumn(name = "id_расписания")])
        var favoriteSchedules: Set<Schedule> = emptySet(),
)