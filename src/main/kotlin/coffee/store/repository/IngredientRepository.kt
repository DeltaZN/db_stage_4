package coffee.store.repository

import coffee.store.dao.Ingredient

interface IngredientRepository {
    fun save(ingredient: Ingredient): Long
    fun update(ingredient: Ingredient): Int
    fun deleteById(id: Long): Int
    fun findAll(): List<Ingredient>
    fun findById(id: Long): Ingredient?
}