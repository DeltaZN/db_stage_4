package coffee.store.dao

import coffee.store.auth.ERole
import coffee.store.entity.Sex
import java.time.LocalDate

data class User(
        var id: Long,
        val firstName: String,
        val lastName: String,
        val sex: Sex,
        val birthDay: LocalDate?,
        val address: Address?,
        val email: String?,
        val phone: String,
        val password: String,
        val role: ERole,
)