package coffee.store.repository

import coffee.store.entity.Order
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import java.util.*

interface OrderJpaRepository : CrudRepository<Order, Long> {
    @Query(
            value = "select * from заказ where id_клиента = ?",
            countQuery = "select count(*) from заказ where id_клиента = ?",
            nativeQuery = true)
    fun findOrderByUser(userId: Long): Iterable<Order>

    @Query(
            value = "select * from заказ where статус_заказа != 'GIVEN' and id_кофейни = ?",
            countQuery = "select count(*) from заказ where статус_заказа != 'GIVEN' and id_кофейни = ?",
            nativeQuery = true)
    fun findIncompleteOrders(coffeeStoreId: Long): Iterable<Order>

    @Query(
            value = "select * from заказ where статус_заказа != 'GIVEN' and id = ?",
            countQuery = "select 1",
            nativeQuery = true)
    fun findIncompleteOrder(orderId: Long): Optional<Order>
}