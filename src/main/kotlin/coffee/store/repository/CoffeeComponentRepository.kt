package coffee.store.repository

import coffee.store.dao.CoffeeComponent

interface CoffeeComponentRepository {
    fun save(coffeeComponent: CoffeeComponent): Long
    fun update(coffeeComponent: CoffeeComponent): Int
    fun deleteById(id: Long): Int
    fun findAll(): List<CoffeeComponent>
    fun findAllByCoffeeId(id: Long): MutableList<CoffeeComponent>
    fun findById(id: Long): CoffeeComponent?
}