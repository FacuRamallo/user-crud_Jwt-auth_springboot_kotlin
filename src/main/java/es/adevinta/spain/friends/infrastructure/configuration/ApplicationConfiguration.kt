package es.adevinta.spain.friends.infrastructure.configuration

import es.adevinta.spain.friends.application.auth.RegisterUser
import es.adevinta.spain.friends.application.GetUsers
import es.adevinta.spain.friends.application.auth.AuthenticateUser
import es.adevinta.spain.friends.domain.contracts.UserAuthenticationService
import es.adevinta.spain.friends.domain.contracts.UserRepository
import es.adevinta.spain.friends.infrastructure.auth.services.PasswordEncoderServiceImpl
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
  fun getUser(userRepository : UserRepository) = GetUsers(userRepository)

  @Bean
  fun passwordEncoderService(passwordEncoder: PasswordEncoder) = PasswordEncoderServiceImpl(passwordEncoder)
}
