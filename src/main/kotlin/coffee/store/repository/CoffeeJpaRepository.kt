package coffee.store.repository

import coffee.store.entity.Coffee
import org.springframework.data.repository.CrudRepository

interface CoffeeJpaRepository : CrudRepository<Coffee, Long>