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
        open val name: String = "",
        @Column(name = "стоимость")
        open val cost: Double = 0.0,
        @Column(name = "фото")
        open val photo: ByteArray? = null,
)