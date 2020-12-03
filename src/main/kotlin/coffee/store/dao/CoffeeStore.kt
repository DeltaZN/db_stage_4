package coffee.store.dao

import javax.persistence.*

@Entity
@Table(name = "кофейня")
data class CoffeeStore(
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        var id: Long = 0,
        @ManyToOne
        @JoinColumn(name = "id_адреса")
        val address: Address? = null,
        @Column(name = "телефон")
        val phone: String = "",
)