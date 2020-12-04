package coffee.store.repository.jpa

import coffee.store.entity.Coffee
import org.springframework.data.repository.CrudRepository

interface CoffeeJpaRepository : CrudRepository<Coffee, Long>