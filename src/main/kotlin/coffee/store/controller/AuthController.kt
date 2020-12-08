package coffee.store.controller

import coffee.store.auth.ERole
import coffee.store.auth.JwtUtils
import coffee.store.auth.UserDetailsImpl
import coffee.store.entity.User
import coffee.store.payload.request.LoginRequest
import coffee.store.payload.request.SignupRequest
import coffee.store.payload.response.JwtResponse
import coffee.store.payload.response.MessageResponse
import coffee.store.repository.RoleJpaRepository
import coffee.store.repository.UserJpaRepository
import io.swagger.annotations.Api
import org.springframework.http.ResponseEntity
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.web.bind.annotation.*
import java.util.stream.Collectors


@CrossOrigin(origins = ["*"], maxAge = 3600)
@RestController
@RequestMapping("/api/auth")
@Api(tags = ["Authentication"])
class AuthController(
        private val authenticationManager: AuthenticationManager,
        private val userRepository: UserJpaRepository,
        private val roleJpaRepository: RoleJpaRepository,
        private val encoder: PasswordEncoder,
        private val jwtUtils: JwtUtils,
) {
    @PostMapping("/signin")
    fun authenticateUser(@RequestBody loginRequest: LoginRequest): ResponseEntity<*>? {
        val authentication: Authentication = authenticationManager.authenticate(
                UsernamePasswordAuthenticationToken(loginRequest.phone, loginRequest.password))
        SecurityContextHolder.getContext().authentication = authentication
        val jwt = jwtUtils.generateJwtToken(authentication)
        val userDetails = authentication.principal as UserDetailsImpl
        return ResponseEntity.ok<Any>(JwtResponse(
                userDetails.username,
                userDetails.authorities.stream()
                        .map { v -> v.authority }
                        .collect(Collectors.toList()),
                jwt,
        ))
    }

    @PostMapping("/signup")
    fun registerUser(@RequestBody signUpRequest: SignupRequest): ResponseEntity<Any> {
        if (userRepository.findByPhone(signUpRequest.phone).isPresent)
            return ResponseEntity
                    .badRequest()
                    .body(MessageResponse("Error: Username is already taken!"))

        val user = User(0, signUpRequest.name, signUpRequest.surname, signUpRequest.sex,
                null, null, null,
                signUpRequest.phone, encoder.encode(signUpRequest.password),
                setOf(roleJpaRepository.findRoleByName(ERole.ROLE_CUSTOMER).get()))
        userRepository.save(user)
        return ResponseEntity.ok(MessageResponse("User registered successfully!"))
    }

}