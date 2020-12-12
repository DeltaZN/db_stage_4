package coffee.store.entity

import coffee.store.model.OrderStatus
import coffee.store.model.Ownerable
import java.time.LocalDateTime
import javax.persistence.*

@Entity
@Table(name = "заказ")
data class Order(
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        val id: Long = 0,
        @Enumerated(EnumType.STRING)
        @Column(name = "статус_заказа")
        var status: OrderStatus = OrderStatus.FORMING,
        @ManyToOne
        @JoinColumn(name = "id_клиента")
        val user: User = User(),
        @ManyToOne
        @JoinColumn(name = "id_кофейни")
        val coffeeStore: CoffeeStore? = null,
        @Column(name = "скидка")
        val discount: Double? = 0.0,
        @Column(name = "стоимость")
        val cost: Double = 0.0,
        @Column(name = "время_формирования")
        val orderTime: LocalDateTime? = null,
        @OneToMany(fetch = FetchType.LAZY, mappedBy = "order")
        var items: List<OrderItem> = mutableListOf(),
) : Ownerable {
        override val owner: User
                get() = user
}