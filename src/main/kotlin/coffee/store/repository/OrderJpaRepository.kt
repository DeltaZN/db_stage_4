package coffee.store.repository

import coffee.store.entity.Order
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository

interface OrderJpaRepository : CrudRepository<Order, Long> {
    @Query(
            value = "select * from заказ where id_клиента = ?",
            countQuery = "select count(*) from заказ",
            nativeQuery = true)
    fun findOrderByUser(userId: Long): Iterable<Order>
}