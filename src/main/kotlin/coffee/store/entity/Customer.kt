package coffee.store.entity

import java.time.LocalDate

data class Customer(
        var id: Long,
        var firstName: String,
        var lastName: String,
        var sex: Sex,
        var birthDay: LocalDate?,
        var address: Address?,
        var email: String?,
        var phone: String
)