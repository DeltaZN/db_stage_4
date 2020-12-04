package coffee.store.repository

import coffee.store.entity.CoffeeScore
import org.springframework.data.repository.CrudRepository

interface CoffeeScoreJpaRepository : CrudRepository<CoffeeScore, Long>