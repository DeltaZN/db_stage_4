package coffee.store.repository.jpa

import coffee.store.entity.CoffeeStore
import org.springframework.data.repository.CrudRepository

interface CoffeeStoreJpaRepository : CrudRepository<CoffeeStore, Long>