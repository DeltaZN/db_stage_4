package coffee.store.repository

import coffee.store.entity.OrderComponent
import org.springframework.data.repository.CrudRepository

interface OrderComponentJpaRepository : CrudRepository<OrderComponent, Long>