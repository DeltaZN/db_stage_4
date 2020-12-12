package coffee.store.service

import coffee.store.entity.Coffee
import coffee.store.entity.CoffeeComponent
import coffee.store.model.CoffeeType
import coffee.store.payload.request.constructor.CoffeeConstructComponent
import coffee.store.payload.request.constructor.CoffeeConstructRequest
import coffee.store.payload.response.CoffeeFullItemComponent
import coffee.store.payload.response.CoffeeFullItemResponse
import coffee.store.repository.CoffeeComponentJpaRepository
import coffee.store.repository.CoffeeJpaRepository
import coffee.store.repository.CoffeeScoreJpaRepository
import coffee.store.repository.IngredientJpaRepository
import org.springframework.stereotype.Service
import javax.persistence.EntityNotFoundException
import javax.transaction.Transactional

@Service
class CoffeeService(
        private val coffeeJpaRepository: CoffeeJpaRepository,
        private val coffeeComponentJpaRepository: CoffeeComponentJpaRepository,
        private val userService: UserService,
        private val ingredientJpaRepository: IngredientJpaRepository,
        private val coffeeScoreJpaRepository: CoffeeScoreJpaRepository,
) {

    @Transactional
    fun getCoffee(id: Long): CoffeeFullItemResponse {
        val coffee = coffeeJpaRepository.findById(id)
                .orElseThrow { EntityNotFoundException("Coffee not found - $id") }
        val components = coffee.components.asIterable()
                .map { c ->
                    CoffeeFullItemComponent(c.ingredient.name, c.addingOrder,
                            c.quantity, c.ingredient.volumeMl)
                }
        return CoffeeFullItemResponse(coffee.id, coffee.name, coffee.cost, coffee.type,
                "${coffee.author.firstName} ${coffee.author.lastName}", coffee.status,
                coffeeScoreJpaRepository.getAverageScoreByCoffee(coffee.id), coffee.photo, components)
    }

    fun addCoffee(payload: CoffeeConstructRequest, coffeeType: CoffeeType) {
        val user = userService.getUserFromAuth()
        val coffee = Coffee()
        var totalCost = 0.0
        payload.components?.let {
            val transformed = transformCoffeeComponents(it, coffee)
            coffee.components = transformed.first
            totalCost = transformed.second
        }
        coffee.author = user
        coffee.status = payload.status!!
        coffee.name = payload.name!!
        coffee.type = coffeeType
        coffee.photo = payload.photo
        coffee.cost = totalCost
        coffeeJpaRepository.save(coffee)
        coffee.components.forEach { c -> coffeeComponentJpaRepository.save(c) }
    }

    fun editCustomCoffee(payload: CoffeeConstructRequest) {
        val coffee = coffeeJpaRepository.findById(payload.id!!)
                .orElseThrow { EntityNotFoundException("Coffee not found ${payload.id}") }
        userService.checkAuthority(coffee)
        payload.components?.let {
            val transformed = transformCoffeeComponents(it, coffee)
            coffee.components = transformed.first
            coffee.cost = transformed.second
        }
        payload.status?.let { coffee.status = it }
        payload.name?.let { coffee.name = it }
        payload.photo?.let { coffee.photo = it }
        coffeeJpaRepository.save(coffee)
    }

    @Transactional
    fun copyAndGetCoffee(id: Long): CoffeeFullItemResponse {
        val user = userService.getCurrentUserId()
        val newId = coffeeJpaRepository.copyCoffee(id, user)
        return getCoffee(newId)
    }

    private fun transformCoffeeComponents(components: List<CoffeeConstructComponent>, coffee: Coffee): Pair<List<CoffeeComponent>, Double> {
        var totalCost = 0.0
        val transformed = components.asIterable().map { c ->
            val ingredient = ingredientJpaRepository.findById(c.ingredientId)
                    .orElseThrow { EntityNotFoundException("Ingredient not found - ${c.ingredientId}") }
            totalCost += c.quantity * ingredient.cost
            CoffeeComponent(0, coffee, ingredient, c.quantity, c.addingOrder)
        }
        return Pair(transformed, totalCost)
    }

}