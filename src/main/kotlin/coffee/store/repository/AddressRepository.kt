package coffee.store.repository

import coffee.store.dao.Address
import java.util.*

interface AddressRepository {
    fun save(address: Address): Long
    fun update(address: Address): Int
    fun deleteById(id: Long): Int
    fun findAll(): List<Address>
    fun findById(id: Long): Optional<Address>
}