package com.facundo.friends.infrastructure.auth.services

import com.facundo.friends.application.auth.dtos.AuthUserDto
import com.facundo.friends.domain.contracts.UserAuthenticationService
import com.facundo.friends.infrastructure.auth.CustomUserDetailsImpl
import com.facundo.friends.infrastructure.auth.JwtUtils
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
