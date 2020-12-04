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
        val firstName: String = "",
        @Column(name = "фамилия")
        val lastName: String = "",
        @Column(name = "пол")
        @Enumerated(EnumType.STRING)
        val sex: Sex = Sex.M,
        @Column(name = "дата_рождения")
        val birthDay: LocalDate? = null,
        @ManyToOne
        @JoinColumn(name = "id_адреса")
        val address: Address? = null,
        @Column(name = "email")
        val email: String? = null,
        @Column(name = "телефон")
        val phone: String = "",
        @Column(name = "пароль")
        val password: String = "",
        @ManyToMany(fetch = FetchType.LAZY)
        @JoinTable(name = "роли_пользователей",
                joinColumns = [JoinColumn(name = "id_клиента")],
                inverseJoinColumns = [JoinColumn(name = "id_роли")])
        val role: Set<Role> = emptySet(),
)