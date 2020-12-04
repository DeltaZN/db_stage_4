package coffee.store.entity

import javax.persistence.*

@Table(name = "ингредиент")
@Entity
data class Ingredient(
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        var id: Long = 0,
        @Column(name = "название", nullable = false)
        val name: String = "",
        @Column(name = "стоимость", nullable = false)
        val cost: Double = 0.0,
        @Column(name = "количество_мл", nullable = false)
        val volumeMl: Double = 0.0
)