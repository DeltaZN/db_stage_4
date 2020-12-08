package coffee.store.service

import coffee.store.auth.UserDetailsImpl
import coffee.store.entity.User
import coffee.store.repository.UserJpaRepository
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service

@Service
class UserService(
        private val userJpaRepository: UserJpaRepository,
) {
    fun getCurrentUserId(): Long = (SecurityContextHolder.getContext().authentication.principal as UserDetailsImpl).id

    fun getUserFromAuth(): User = userJpaRepository.findById(getCurrentUserId())
            .orElseThrow { UsernameNotFoundException("User not found - ${getCurrentUserId()}") }
}