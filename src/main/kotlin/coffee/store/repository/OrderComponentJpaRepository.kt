package coffee.store.repository

import coffee.store.entity.OrderItem
import org.springframework.data.repository.CrudRepository

interface OrderComponentJpaRepository : CrudRepository<OrderItem, Long>