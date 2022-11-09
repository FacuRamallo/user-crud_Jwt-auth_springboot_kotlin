package com.facundo.friends.infrastructure.configuration

import com.facundo.friends.application.auth.RegisterUser
import com.facundo.friends.application.GetFriendsList
import com.facundo.friends.application.auth.AuthenticateUser
import com.facundo.friends.domain.contracts.FriendshipRepository
import com.facundo.friends.domain.contracts.UserAuthenticationService
import com.facundo.friends.domain.contracts.UserRepository
import com.facundo.friends.infrastructure.auth.services.PasswordEncoderServiceImpl
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.crypto.password.PasswordEncoder

@Configuration
class ApplicationConfiguration {

  @Bean
  fun registerUser(
    userRepository : UserRepository,
    passwordEncoderService: PasswordEncoderServiceImpl
  ) = RegisterUser(userRepository, passwordEncoderService)

  @Bean
  fun authenticateUser(userAuthenticationService: UserAuthenticationService
  ) = AuthenticateUser(userAuthenticationService)

  @Bean
  fun getFriendsList(
    friendshipRepository : FriendshipRepository,
    userAuthenticationService: UserAuthenticationService
  ) = GetFriendsList(friendshipRepository,userAuthenticationService)

  @Bean
  fun passwordEncoderService(passwordEncoder: PasswordEncoder) = PasswordEncoderServiceImpl(passwordEncoder)
}
