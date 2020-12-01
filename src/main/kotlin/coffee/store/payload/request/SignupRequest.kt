package coffee.store.payload.request

import coffee.store.entity.Sex

data class SignupRequest(
        val phone: String,
        val password: String,
        val name: String,
        val surname: String,
        val sex: Sex,
)