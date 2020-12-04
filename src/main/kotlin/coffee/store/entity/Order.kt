package coffee.store.entity

import java.time.LocalDateTime

class Order(val id: Long, val status: String, val user: User, val coffeeStore: CoffeeStore,
            val discount: Double?, val cost: Double?, val orderTime: LocalDateTime?)