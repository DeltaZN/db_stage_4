package coffee.store.repository.jpa

import coffee.store.entity.Dessert
import org.springframework.data.repository.CrudRepository

interface DessertJpaRepository : CrudRepository<Dessert, Long>