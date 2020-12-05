package coffee.store.entity

import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.PrimaryKeyJoinColumn
import javax.persistence.Table

@Entity
@Table(name = "десерт")
@PrimaryKeyJoinColumn(name = "id_товара")
class Dessert(
        id: Long = 0,
        @Column(name = "калории")
        val calories: Double = 0.0,
        @Column(name = "вес")
        val weight: Double = 0.0,
        name: String = "",
        cost: Double = 0.0,
        photo: ByteArray? = null
) : Product(id, name, cost, photo)