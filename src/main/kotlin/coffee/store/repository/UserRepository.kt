package coffee.store.repository

import coffee.store.dao.User
import java.util.*


interface UserRepository {
    fun save(user: User): Long
    fun update(user: User): Int
    fun deleteById(id: Long): Int
    fun findAll(): List<User>
    fun findById(id: Long): Optional<User>
    fun findByPhone(phone: String): Optional<User>
}