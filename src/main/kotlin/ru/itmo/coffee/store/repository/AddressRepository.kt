package ru.itmo.coffee.store.repository

import ru.itmo.coffee.store.dao.Address

interface AddressRepository {
    fun save(address: Address): Long
    fun update(address: Address): Int
    fun deleteById(id: Long): Int
    fun findAll(): List<Address>
    fun findById(id: Long): Address?
}