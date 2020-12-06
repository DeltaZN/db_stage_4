package coffee.store.payload.common

import coffee.store.entity.Address
import coffee.store.model.Sex
import java.time.LocalDate

data class UserInformationPayload(
        val firstName: String?,
        val lastName: String?,
        val sex: Sex?,
        val birthDay: LocalDate?,
        val address: Address?,
        val email: String?,
        val phone: String?,
        val password: String?,
)
