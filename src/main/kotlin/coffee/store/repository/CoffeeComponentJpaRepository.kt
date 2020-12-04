package coffee.store.repository

import coffee.store.entity.CoffeeComponent
import org.springframework.data.repository.CrudRepository

interface CoffeeComponentJpaRepository : CrudRepository<CoffeeComponent, Long>