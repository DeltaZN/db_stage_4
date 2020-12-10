package coffee.store.entity

import coffee.store.model.CoffeeStatus
import coffee.store.model.CoffeeType
import coffee.store.model.Ownerable
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
        var type: CoffeeType = CoffeeType.u,
        @Enumerated(EnumType.STRING)
        @Column(name = "состояние")
        var status: CoffeeStatus = CoffeeStatus.HIDDEN,
        @ManyToOne
        @JoinColumn(name = "id_автора")
        var author: User = User(),
        @OneToMany(fetch = FetchType.LAZY, mappedBy = "coffee")
        var components: List<CoffeeComponent> = mutableListOf(),
) : Product(id, name, cost, photo), Ownerable {
    override val owner: User
        get() = author
}