package com.facundo.friends.infrastructure.auth

import com.facundo.friends.domain.User
import com.facundo.friends.domain.contracts.UserRepository
import com.facundo.friends.domain.exceptions.UserNameNotFoundException
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.transaction.annotation.Transactional


open class CustomUserDetailsServiceImpl(private val userRepository: UserRepository) : UserDetailsService {

  @Override
  @Transactional
  override fun loadUserByUsername(username: String): UserDetails {
    val user: User = userRepository.getByUserName(username) ?: throw UserNameNotFoundException(username)

    return CustomUserDetailsImpl.build(user)
  }

}
