package coffee.store.entity

import javax.persistence.*

@Table(name = "компонент_кофе")
@Entity
data class CoffeeComponent(
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        var id: Long = 0,
        @ManyToOne
        @JoinColumn(name = "id_кофе")
        val coffee: Coffee? = null,
        @ManyToOne
        @JoinColumn(name = "id_ингредиента")
        val ingredient: Ingredient = Ingredient(),
        @Column(name = "количество")
        val quantity: Int = 0,
        @Column(name = "порядок_добавления")
        val addingOrder: Int = 0,
)