package ru.itmo.coffee.store.repository

import ru.itmo.coffee.store.model.Coffee

interface CoffeeRepository {
    fun save(coffee: Coffee): Int
    fun update(coffee: Coffee): Int
    fun deleteById(id: Long): Int
    fun findAll(): List<Coffee>
    fun findById(id: Long): Coffee?
}