package coffee.store.repository

import coffee.store.entity.Coffee
import coffee.store.model.CoffeeStatus
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository

interface CoffeeJpaRepository : CrudRepository<Coffee, Long> {
    @Query(
            value = "select * from кофе JOIN товар on кофе.id_товара = товар.id where состояние = 'PUBLISHED'",
            countQuery = "select count(*) from кофе",
            nativeQuery = true)
    fun findAllPublicCoffee(): Iterable<Coffee>
}