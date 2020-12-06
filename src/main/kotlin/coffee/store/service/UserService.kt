package coffee.store.service

import coffee.store.auth.UserDetailsImpl
import coffee.store.repository.UserJpaRepository
import org.springframework.security.core.Authentication
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service

@Service
class UserService(
        private val userJpaRepository: UserJpaRepository,
) {
    fun getUserFromAuth(auth: Authentication) = userJpaRepository.findById((auth.principal as UserDetailsImpl).id)
            .orElseThrow { UsernameNotFoundException("User not found - ${(auth.principal as UserDetailsImpl).id}") }
}