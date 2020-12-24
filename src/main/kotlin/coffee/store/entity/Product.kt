package coffee.store.entity

import javax.persistence.*

@Entity
@Table(name = "товар")
@Inheritance(strategy = InheritanceType.JOINED)
abstract class Product(
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        open var id: Long = 0,
        @Column(name = "название")
        open var name: String = "",
        @Column(name = "стоимость")
        open var cost: Double = 0.0,
        @Column(name = "фото")
        open var photo: String? = null,
)