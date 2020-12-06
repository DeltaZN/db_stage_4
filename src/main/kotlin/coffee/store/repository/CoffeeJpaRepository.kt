package coffee.store.repository

import coffee.store.entity.Coffee
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository

interface CoffeeJpaRepository : CrudRepository<Coffee, Long> {
    @Query(
            value = "select * from кофе JOIN товар on кофе.id_товара = товар.id where состояние = 'PUBLISHED'",
            countQuery = "select count(*) from кофе where состояние = 'PUBLISHED'",
            nativeQuery = true)
    fun findAllPublicCoffee(): Iterable<Coffee>

    @Query(
            value = "select * from кофе JOIN товар on кофе.id_товара = товар.id where id_автора = ?",
            countQuery = "select count(*) from кофе where id_автора = ?",
            nativeQuery = true)
    fun findUserCoffees(userId: Long): Iterable<Coffee>
}