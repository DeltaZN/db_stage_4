package coffee.store.repository

import coffee.store.entity.CoffeeStore
import org.springframework.data.repository.CrudRepository

interface CoffeeStoreJpaRepository : CrudRepository<CoffeeStore, Long>