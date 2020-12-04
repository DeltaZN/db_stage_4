package coffee.store.entity

import coffee.store.auth.ERole
import javax.persistence.*

@Entity
@Table(name = "роль")
data class Role(
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        val id: Long = 0,
        @Enumerated(EnumType.STRING)
        @Column(name = "название")
        val name: ERole = ERole.ROLE_CUSTOMER,
)