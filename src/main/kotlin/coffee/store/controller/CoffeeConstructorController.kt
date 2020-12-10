package coffee.store.controller

import coffee.store.entity.Ingredient
import coffee.store.model.CoffeeType
import coffee.store.payload.request.constructor.CoffeeConstructRequest
import coffee.store.payload.response.CoffeeListItemResponse
import coffee.store.payload.response.MessageResponse
import coffee.store.repository.CoffeeJpaRepository
import coffee.store.repository.IngredientJpaRepository
import coffee.store.service.CoffeeService
import coffee.store.service.UserService
import io.swagger.annotations.Api
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*

@CrossOrigin(origins = ["*"], maxAge = 3600)
@RestController
@RequestMapping("/api/constructor")
@Api(tags = ["Constructor"])
@PreAuthorize("hasRole('CUSTOMER')")
class CoffeeConstructorController(
        private val ingredientJpaRepository: IngredientJpaRepository,
        private val coffeeJpaRepository: CoffeeJpaRepository,
        private val userService: UserService,
        private val coffeeService: CoffeeService,
) {

    @GetMapping("ingredients")
    fun getIngredients(): List<Ingredient> = ingredientJpaRepository.findAll().toList()

    @GetMapping("coffees")
    fun getCustomCoffees(): List<CoffeeListItemResponse> =
            coffeeJpaRepository.findUserCoffees(userService.getCurrentUserId())
                    .map { c -> CoffeeListItemResponse(c.id, c.name, c.cost, CoffeeType.valueOf(c.type), c.avgRating, c.photo) }

    @PostMapping("coffees")
    fun addCustomCoffee(@RequestBody payload: CoffeeConstructRequest): MessageResponse {
        coffeeService.addCoffee(payload, CoffeeType.u)
        return MessageResponse("Successfully added custom coffee!")
    }

    @PostMapping("copy_coffee/{id}")
    fun copyCoffee(@PathVariable id: Long) = coffeeService.copyAndGetCoffee(id)

    @PutMapping("coffees")
    fun editCustomCoffee(@RequestBody payload: CoffeeConstructRequest): MessageResponse {
        coffeeService.editCustomCoffee(payload)
        return MessageResponse("Successfully edited coffee!")
    }
}