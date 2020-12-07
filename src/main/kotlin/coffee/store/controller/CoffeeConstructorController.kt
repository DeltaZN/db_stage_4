package coffee.store.controller

import coffee.store.auth.UserDetailsImpl
import coffee.store.entity.Coffee
import coffee.store.entity.CoffeeComponent
import coffee.store.entity.Ingredient
import coffee.store.model.CoffeeType
import coffee.store.payload.request.constructor.CoffeeConstructRequest
import coffee.store.payload.response.CoffeeListItemResponse
import coffee.store.payload.response.MessageResponse
import coffee.store.repository.CoffeeJpaRepository
import coffee.store.repository.IngredientJpaRepository
import coffee.store.repository.UserJpaRepository
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.core.Authentication
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.web.bind.annotation.*
import javax.persistence.EntityNotFoundException

@CrossOrigin(origins = ["*"], maxAge = 3600)
@RestController
@RequestMapping("/api/constructor")
@PreAuthorize("hasRole('CUSTOMER')")
class CoffeeConstructorController(
        private val ingredientJpaRepository: IngredientJpaRepository,
        private val coffeeJpaRepository: CoffeeJpaRepository,
        private val userJpaRepository: UserJpaRepository,
) {

    private fun getUser(auth: Authentication) = userJpaRepository.findById((auth.principal as UserDetailsImpl).id)
            .orElseThrow { UsernameNotFoundException("User not found - ${(auth.principal as UserDetailsImpl).id}") }

    @GetMapping("ingredients")
    fun getIngredients(): Iterable<Ingredient> = ingredientJpaRepository.findAll()

    @GetMapping("coffees")
    fun getCustomCoffees(auth: Authentication): List<CoffeeListItemResponse> =
            coffeeJpaRepository.findUserCoffees((auth.principal as UserDetailsImpl).id)
                    .map { c -> CoffeeListItemResponse(c.id, c.name, c.cost, CoffeeType.valueOf(c.type), c.avgRating, c.photo) }

    @PostMapping("coffees")
    fun addCustomCoffee(auth: Authentication, payload: CoffeeConstructRequest): MessageResponse {
        val user = getUser(auth)
        val coffee = Coffee()
        var totalCost = 0.0
        payload.components?.let {
            coffee.components = it.asIterable().map { c ->
                val ingredient = ingredientJpaRepository.findById(c.ingredientId)
                        .orElseThrow { EntityNotFoundException("Ingredient not found - ${c.ingredientId}") }
                totalCost += c.quantity * ingredient.cost
                CoffeeComponent(0, coffee, ingredient, c.quantity, c.addingOrder)
            }
        }
        coffee.author = user
        coffee.status = payload.status!!
        coffee.name = payload.name!!
        coffee.photo = payload.photo
        coffee.cost = totalCost
        coffeeJpaRepository.save(coffee)
        return MessageResponse("Successfully added coffee!")
    }

    @PutMapping("coffees")
    fun editCustomCoffee(auth: Authentication, payload: CoffeeConstructRequest): MessageResponse {
        val user = getUser(auth)
        val coffee = coffeeJpaRepository.findById(payload.id!!)
                .orElseThrow { EntityNotFoundException("Coffee not found ${payload.id}") }
        if (coffee.author?.id != user.id)
            throw IllegalAccessException("An access try to wrong schedule!")

        var totalCost = 0.0
        payload.components?.let {
            coffee.components = it.asIterable().map { c ->
                val ingredient = ingredientJpaRepository.findById(c.ingredientId)
                        .orElseThrow { EntityNotFoundException("Ingredient not found - ${c.ingredientId}") }
                totalCost += c.quantity * ingredient.cost
                CoffeeComponent(0, coffee, ingredient, c.quantity, c.addingOrder)
            }
            coffee.cost = totalCost
        }
        payload.status?.let { coffee.status = it }
        payload.name?.let { coffee.name = it }
        payload.photo?.let { coffee.photo = it }
        coffeeJpaRepository.save(coffee)
        return MessageResponse("Successfully edited coffee!")
    }
}