package coffee.store.payload.request

data class LoginRequest(
        val phone: String,
        val password: String
)