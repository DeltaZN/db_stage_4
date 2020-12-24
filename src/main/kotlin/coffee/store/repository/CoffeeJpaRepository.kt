package coffee.store.repository

import coffee.store.entity.Coffee
import coffee.store.model.CoffeeStatus
import coffee.store.model.CoffeeType
import coffee.store.repository.projection.CoffeeListItemProjection
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository

interface CoffeeJpaRepository : CrudRepository<Coffee, Long> {
    @Query(
            value = "select товар.id as id, название as name, стоимость as cost, тип as type, фото as photo, avg(оценка.оценка) as avgRating from кофе " +
                    "JOIN товар on кофе.id_товара = товар.id LEFT OUTER JOIN оценка_кофе ок on кофе.id_товара = ок.id_кофе " +
                    "LEFT OUTER JOIN оценка on ок.id_оценки = оценка.id where состояние = 'PUBLISHED' group by товар.id, название, стоимость, тип, фото",
            countQuery = "select count(*) from кофе where состояние = 'PUBLISHED'",
            nativeQuery = true)
    fun findAllPublicCoffee(): Iterable<CoffeeListItemProjection>

    @Query(
            value = "select товар.id as id, название as name, стоимость as cost, тип as type, фото as photo, avg(оценка.оценка) as avgRating from кофе " +
                    "JOIN товар on кофе.id_товара = товар.id LEFT OUTER JOIN оценка_кофе ок on кофе.id_товара = ок.id_кофе " +
                    "LEFT OUTER JOIN оценка on ок.id_оценки = оценка.id where id_автора = ? group by товар.id, название, стоимость, тип, фото",
            countQuery = "select count(*) from кофе where id_автора = ?",
            nativeQuery = true)
    fun findUserCoffees(userId: Long): Iterable<CoffeeListItemProjection>

    @Query(
            value = "select createCoffee(?, ?, ?, ?, ?, ?)",
            countQuery = "select 1",
            nativeQuery = true)
    fun storeCoffee(name: String, cost: Double, photo: String?,
                    coffeeType: CoffeeType, authorId: Long, coffeeStatus: CoffeeStatus): Long

    @Query(
            value = "select copyCoffee(?, ?)",
            countQuery = "select 1",
            nativeQuery = true)
    fun copyCoffee(scheduleId: Long, clientId: Long): Long
}