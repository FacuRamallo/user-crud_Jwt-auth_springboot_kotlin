package es.adevinta.spain.friends.infrastructure.auth.services

import es.adevinta.spain.friends.application.auth.dtos.AuthUserDto
import es.adevinta.spain.friends.domain.contracts.UserAuthenticationService
import es.adevinta.spain.friends.infrastructure.auth.CustomUserDetailsImpl
import es.adevinta.spain.friends.infrastructure.auth.JwtUtils
import java.util.stream.Collectors
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContextHolder

class UserAuthenticationServiceImpl(private val authenticationManager: AuthenticationManager, private val jwtUtils: JwtUtils): UserAuthenticationService {

  override fun authenticateUser(username: String, password: String){
    val authentication = authenticationManager.authenticate( UsernamePasswordAuthenticationToken(username, password) )
    setSecurityContextAuthentication(authentication)
  }

  private fun setSecurityContextAuthentication(authentication: Authentication) {
    SecurityContextHolder.getContext().authentication = authentication
  }

  private fun getSecurityContextAuthentication(): Authentication {
    return SecurityContextHolder.getContext().authentication
  }

  override fun getAuthenticatedUserToken(): String {
    val authentication = getSecurityContextAuthentication()
     return jwtUtils.generateJwt(authentication)
  }

  override fun getAuthenticatedUserDetails() : AuthUserDto {
    val authUserDetails = getSecurityContextAuthentication().principal as CustomUserDetailsImpl

    val roles: List<String> = authUserDetails.authorities.map { item -> item.authority }

    return AuthUserDto(
      getAuthenticatedUserToken(),
      "Bearer",
      authUserDetails.username,
      roles)
  }

  override fun getAuthenticatedUserName(): String {
    val authUserDetails = getSecurityContextAuthentication().principal as CustomUserDetailsImpl
    return authUserDetails.username
  }


}
