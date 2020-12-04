package coffee.store.repository

import coffee.store.entity.Ingredient
import org.springframework.data.repository.CrudRepository

interface IngredientJpaRepository : CrudRepository<Ingredient, Long>