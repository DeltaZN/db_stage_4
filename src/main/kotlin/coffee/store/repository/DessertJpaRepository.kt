package coffee.store.repository

import coffee.store.entity.Dessert
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository

interface DessertJpaRepository : CrudRepository<Dessert, Long> {
    @Query(
            value = "select createDessert(?, ?, ?, ?, ?)",
            countQuery = "select 1",
            nativeQuery = true)
    fun storeDessert(name: String, cost: Double, photo: ByteArray?,
                     calories: Double, weight: Double): Long
}