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
import coffee.store.service.UserService
import io.swagger.annotations.Api
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.core.Authentication
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.web.bind.annotation.*
import javax.persistence.EntityNotFoundException

@CrossOrigin(origins = ["*"], maxAge = 3600)
@RestController
@RequestMapping("/api/constructor")
@Api(tags = ["Constructor"])
@PreAuthorize("hasRole('CUSTOMER')")
class CoffeeConstructorController(
        private val ingredientJpaRepository: IngredientJpaRepository,
        private val coffeeJpaRepository: CoffeeJpaRepository,
        private val userService: UserService,
) {

    @GetMapping("ingredients")
    fun getIngredients(): List<Ingredient> = ingredientJpaRepository.findAll().toList()

    @GetMapping("coffees")
    fun getCustomCoffees(): List<CoffeeListItemResponse> =
            coffeeJpaRepository.findUserCoffees(userService.getCurrentUserId())
                    .map { c -> CoffeeListItemResponse(c.id, c.name, c.cost, CoffeeType.valueOf(c.type), c.avgRating, c.photo) }

    @PostMapping("coffees")
    fun addCustomCoffee(@RequestBody payload: CoffeeConstructRequest): MessageResponse {
        val user = userService.getUserFromAuth()
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
    fun editCustomCoffee(@RequestBody payload: CoffeeConstructRequest): MessageResponse {
        val user = userService.getUserFromAuth()
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