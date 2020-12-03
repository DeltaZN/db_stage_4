package coffee.store.repository.jpa

import coffee.store.dao.CoffeeStore
import org.springframework.data.repository.CrudRepository

interface CoffeeStoreJpaRepository : CrudRepository<CoffeeStore, Long>