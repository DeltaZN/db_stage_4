package ru.itmo.coffee.store.repository

import ru.itmo.coffee.store.model.Customer


interface CustomerRepository {
    fun save(customer: Customer): Int
    fun update(customer: Customer): Int
    fun deleteById(id: Long): Int
    fun findAll(): List<Customer>
    fun findById(id: Long): Customer?
    fun getNameById(id: Long): String?
}