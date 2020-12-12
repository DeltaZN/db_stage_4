package coffee.store.service

import coffee.store.auth.ERole
import coffee.store.auth.UserDetailsImpl
import coffee.store.entity.User
import coffee.store.model.Ownerable
import coffee.store.payload.common.UserInformationPayload
import coffee.store.payload.response.MessageResponse
import coffee.store.repository.UserJpaRepository
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

    fun checkAuthority(item: Ownerable) {
        val accessor = getUserFromAuth()
        if (accessor.roles.any { r -> r.name == ERole.ROLE_ADMIN })
            return
        if (item.owner.id != accessor.id)
            throw IllegalAccessException("Access denied")
    }

    fun editUser(payload: UserInformationPayload): MessageResponse {
        val user = getUserFromAuth()
        payload.firstName?.let { user.firstName = it }
        payload.lastName?.let { user.lastName = it }
        payload.sex?.let { user.sex = it }
        payload.birthDay?.let { user.birthDay = it }
        payload.password?.let { user.password = it }
        payload.phone?.let { user.phone = it }
        payload.address?.let { user.address = it }
        payload.email?.let { user.email = it }
        userJpaRepository.save(user)
        return MessageResponse("Successfully updated user information")
    }
}