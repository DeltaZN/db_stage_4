package coffee.store.repository

import coffee.store.dao.Coffee

interface CoffeeRepository {
    fun save(coffee: Coffee): Long
    fun update(coffee: Coffee): Int
    fun deleteById(id: Long): Int
    fun findAll(): List<Coffee>
    fun findById(id: Long): Coffee?
}