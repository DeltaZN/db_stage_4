package coffee.store.repository

import coffee.store.dao.CoffeeStore

interface CoffeeStoreRepository {
    fun save(coffeeStore: CoffeeStore): Long
    fun update(coffeeStore: CoffeeStore): Int
    fun deleteById(id: Long): Int
    fun findAll(): List<CoffeeStore>
    fun findById(id: Long): CoffeeStore?
}