package coffee.store.service

import coffee.store.auth.UserDetailsImpl
import coffee.store.repository.UserJpaRepository
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service

@Service
class UserDetailsServiceImpl(private val userRepository: UserJpaRepository) : UserDetailsService {
    override fun loadUserByUsername(username: String): UserDetails =
            UserDetailsImpl.build(userRepository.findByPhone(username)
                    .orElseThrow { UsernameNotFoundException("Username not found - $username") })
}