package es.adevinta.spain.friends.application.auth

import es.adevinta.spain.friends.auth.CustomUserDetailsImpl
import es.adevinta.spain.friends.auth.JwtUtils
import es.adevinta.spain.friends.domain.contracts.UserRepository
import es.adevinta.spain.friends.infrastructure.controller.dtos.LoggedUserDto
import es.adevinta.spain.friends.infrastructure.controller.dtos.SignInDto
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContextHolder

class AuthenticateUser(
  val userRepository: UserRepository,
  private val authenticationManager: AuthenticationManager,
  private val jwtUtils: JwtUtils,
) {

  fun authenticate(signInDto: SignInDto): LoggedUserDto {
    val authentication: Authentication =
      authenticationManager.authenticate(UsernamePasswordAuthenticationToken(signInDto.userName, signInDto.password))

    SecurityContextHolder.getContext().authentication = authentication
    val jwt = jwtUtils.generateJwt(authentication)

    val userDetails: CustomUserDetailsImpl = authentication.principal as CustomUserDetailsImpl

    val roles: List<String> = userDetails.authorities.map { item -> item.authority }

    return LoggedUserDto(jwt, "Bearer", userDetails.username, roles)
  }
}
