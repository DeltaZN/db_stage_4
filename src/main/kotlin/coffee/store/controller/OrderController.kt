package coffee.store.controller

import coffee.store.entity.CoffeeStore
import coffee.store.payload.response.CoffeeListItemResponse
import coffee.store.payload.response.DessertListItemResponse
import coffee.store.repository.CoffeeJpaRepository
import coffee.store.repository.CoffeeStoreJpaRepository
import coffee.store.repository.DessertJpaRepository
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@CrossOrigin(origins = ["*"], maxAge = 3600)
@RestController
@RequestMapping("/api/order")
class OrderController(
        private val coffeeStoreJpaRepository: CoffeeStoreJpaRepository,
        private val coffeeJpaRepository: CoffeeJpaRepository,
        private val dessertsJpaRepository: DessertJpaRepository
) {
    @GetMapping("stores")
    fun getIngredients(): MutableIterable<CoffeeStore> = coffeeStoreJpaRepository.findAll()

    @GetMapping("coffees")
    fun getCoffees(): List<CoffeeListItemResponse> {
        // TODO добавить среднюю оценку
        return coffeeJpaRepository.findAllPublicCoffee()
                .map { c -> CoffeeListItemResponse(c.id, c.name, c.cost, c.type) }
    }

    @GetMapping("desserts")
    fun getDesserts(): List<DessertListItemResponse> = dessertsJpaRepository.findAll()
            .map { d -> DessertListItemResponse(d.id, d.name, d.cost, d.weight, d.calories, d.photo) }
}