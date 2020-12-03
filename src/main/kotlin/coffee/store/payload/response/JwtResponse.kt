package coffee.store.payload.response

data class JwtResponse(
        val phone: String,
        val roles: Collection<String>,
        val accessToken: String,
)