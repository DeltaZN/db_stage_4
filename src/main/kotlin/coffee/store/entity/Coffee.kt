package coffee.store.entity

import javax.persistence.*

@Entity
@Table(name = "кофе")
@PrimaryKeyJoinColumn(name = "id_товара")
class Coffee(
        id: Long = 0,
        name: String = "",
        cost: Double = 0.0,
        photo: ByteArray? = null,
        @Column(name = "тип")
        val type: Char = 'u',
        @Column(name = "состояние")
        val status: String = "скрыт",
        @ManyToOne
        @JoinColumn(name = "id_автора")
        val author: User? = null,
        @OneToMany(fetch = FetchType.LAZY)
        val components: MutableList<CoffeeComponent> = mutableListOf(),
) : Product(id, name, cost, photo)