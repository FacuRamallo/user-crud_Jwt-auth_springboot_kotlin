package es.adevinta.spain.friends.auth

import es.adevinta.spain.friends.domain.User
import es.adevinta.spain.friends.domain.contracts.UserRepository
import es.adevinta.spain.friends.domain.exceptions.UserNameNotFoundException
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional


open class CustomUserDetailsServiceImpl(private val userRepository: UserRepository) : UserDetailsService {

  @Override
  @Transactional
  override fun loadUserByUsername(username: String?): UserDetails {
    val user: User = userRepository.getByUserName(username) ?: throw UserNameNotFoundException(username)

    return CustomUserDetailsImpl.build(user)
  }

}
