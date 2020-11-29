package ru.itmo.coffee.store.repository

import ru.itmo.coffee.store.dao.Dessert

interface DessertRepository {
    fun save(dessert: Dessert): Long
    fun update(dessert: Dessert): Int
    fun deleteById(id: Long): Int
    fun findAll(): List<Dessert>
    fun findById(id: Long): Dessert?
}