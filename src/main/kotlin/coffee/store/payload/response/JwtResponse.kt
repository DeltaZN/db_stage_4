package coffee.store.payload.response

import org.springframework.security.core.GrantedAuthority

data class JwtResponse(
        val jwt: String,
        val phone: String,
        val roles: Collection<GrantedAuthority>,
)