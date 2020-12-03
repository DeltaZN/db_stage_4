package coffee.store.repository.jpa

import coffee.store.dao.Ingredient
import org.springframework.data.repository.CrudRepository

interface IngredientJpaRepository : CrudRepository<Ingredient, Long>