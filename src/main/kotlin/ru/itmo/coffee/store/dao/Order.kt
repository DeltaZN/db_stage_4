package ru.itmo.coffee.store.dao

import java.time.LocalDateTime

class Order(val id: Long, val status: String, val customer: Customer, val coffeeStore: CoffeeStore,
            val discount: Double?, val cost: Double?, val orderTime: LocalDateTime?)