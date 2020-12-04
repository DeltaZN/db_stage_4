package coffee.store.repository

import coffee.store.entity.Order
import org.springframework.data.repository.CrudRepository

interface OrderJpaRepository : CrudRepository<Order, Long> {
}