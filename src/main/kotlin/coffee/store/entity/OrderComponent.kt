package coffee.store.entity

import javax.persistence.*

@Entity
@Table(name = "компонент_заказа")
data class OrderComponent(
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        val id: Long = 0,
        @ManyToOne
        @JoinColumn(name = "id_заказа")
        val order: Order? = null,
        @ManyToOne
        @JoinColumn(name = "id_товара")
        val product: Product? = null,
)