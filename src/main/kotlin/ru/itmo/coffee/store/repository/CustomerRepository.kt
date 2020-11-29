package ru.itmo.coffee.store.repository

import ru.itmo.coffee.store.dao.Customer


interface CustomerRepository {
    fun save(customer: Customer): Long
    fun update(customer: Customer): Int
    fun deleteById(id: Long): Int
    fun findAll(): List<Customer>
    fun findById(id: Long): Customer?
}