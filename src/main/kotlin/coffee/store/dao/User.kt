package coffee.store.dao

import coffee.store.auth.ERole
import coffee.store.entity.Sex
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
        @Column(name = "роль")
        @Enumerated(EnumType.STRING)
        val role: ERole = ERole.ROLE_CUSTOMER,
)