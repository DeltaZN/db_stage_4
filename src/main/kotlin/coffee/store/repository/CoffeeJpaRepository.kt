package coffee.store.repository

import coffee.store.entity.Coffee
import coffee.store.repository.projection.CoffeeListItemProjection
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository

interface CoffeeJpaRepository : CrudRepository<Coffee, Long> {
    @Query(
            value = "select товар.id as id, название as name, стоимость as cost, тип as type, фото as photo, avg(оценка.оценка) as avgRating from кофе " +
                    "JOIN товар on кофе.id_товара = товар.id JOIN оценка_кофе ок on кофе.id_товара = ок.id_кофе " +
                    "JOIN оценка on ок.id_оценки = оценка.id where состояние = 'PUBLISHED' group by товар.id, название, стоимость, тип, фото",
            countQuery = "select count(*) from кофе where состояние = 'PUBLISHED'",
            nativeQuery = true)
    fun findAllPublicCoffee(): Iterable<CoffeeListItemProjection>

    @Query(
            value = "select * from кофе JOIN товар on кофе.id_товара = товар.id where id_автора = ?",
            countQuery = "select count(*) from кофе where id_автора = ?",
            nativeQuery = true)
    fun findUserCoffees(userId: Long): Iterable<Coffee>
}