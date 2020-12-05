package coffee.store.auth

import coffee.store.entity.User
import com.fasterxml.jackson.annotation.JsonIgnore
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import java.util.stream.Collectors


class UserDetailsImpl(
        val id: Long,
        private val phone: String,
        val email: String?,
        @JsonIgnore
        private val password: String,
        private val authorities: Collection<GrantedAuthority>,
) : UserDetails {
    override fun getAuthorities(): Collection<GrantedAuthority> = authorities

    companion object {
        fun build(user: User): UserDetailsImpl {
            val authorities: List<GrantedAuthority> = user.roles.stream()
                    .map { role -> SimpleGrantedAuthority(role.name.toString()) }
                    .collect(Collectors.toList())
            return UserDetailsImpl(
                    user.id,
                    user.phone,
                    user.email,
                    user.password,
                    authorities)
        }
    }

    override fun getPassword(): String = password

    override fun getUsername(): String = phone

    override fun isAccountNonExpired(): Boolean = true

    override fun isAccountNonLocked(): Boolean = true

    override fun isCredentialsNonExpired(): Boolean = true

    override fun isEnabled(): Boolean = true
}