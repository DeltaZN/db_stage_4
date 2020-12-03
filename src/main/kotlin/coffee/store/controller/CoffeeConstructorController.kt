package coffee.store.controller

import coffee.store.dao.Ingredient
import coffee.store.repository.jpa.IngredientJpaRepository
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@CrossOrigin(origins = ["*"], maxAge = 3600)
@RestController
@RequestMapping("/api/constructor")
class CoffeeConstructorController(
        private val ingredientJpaRepository: IngredientJpaRepository
) {
    @GetMapping("ingredients")
    fun getIngredients(): MutableIterable<Ingredient> = ingredientJpaRepository.findAll()
}