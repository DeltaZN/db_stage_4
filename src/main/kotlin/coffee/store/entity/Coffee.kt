package coffee.store.entity

import coffee.store.model.CoffeeStatus
import coffee.store.model.CoffeeType
import javax.persistence.*

@Entity
@Table(name = "кофе")
@PrimaryKeyJoinColumn(name = "id_товара")
class Coffee(
        id: Long = 0,
        name: String = "",
        cost: Double = 0.0,
        photo: ByteArray? = null,
        @Enumerated(EnumType.STRING)
        @Column(name = "тип")
        val type: CoffeeType = CoffeeType.u,
        @Enumerated(EnumType.STRING)
        @Column(name = "состояние")
        val status: CoffeeStatus = CoffeeStatus.HIDDEN,
        @ManyToOne
        @JoinColumn(name = "id_автора")
        val author: User? = null,
        @OneToMany(fetch = FetchType.LAZY, mappedBy = "coffee")
        val components: MutableList<CoffeeComponent> = mutableListOf(),
) : Product(id, name, cost, photo)