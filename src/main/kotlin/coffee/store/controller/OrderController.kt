package coffee.store.controller

import coffee.store.dao.CoffeeStore
import coffee.store.repository.jpa.CoffeeStoreJpaRepository
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@CrossOrigin(origins = ["*"], maxAge = 3600)
@RestController
@RequestMapping("/api/order")
class OrderController(
        private val coffeeStoreJpaRepository: CoffeeStoreJpaRepository,
) {
    @GetMapping("stores")
    fun getIngredients(): MutableIterable<CoffeeStore> = coffeeStoreJpaRepository.findAll()
}