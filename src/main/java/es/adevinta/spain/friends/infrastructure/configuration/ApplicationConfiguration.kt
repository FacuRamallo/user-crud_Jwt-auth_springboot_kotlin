package es.adevinta.spain.friends.infrastructure.configuration

import es.adevinta.spain.friends.application.auth.RegisterUser
import es.adevinta.spain.friends.application.FriendshipHandler
import es.adevinta.spain.friends.application.GetUsers
import es.adevinta.spain.friends.application.auth.AuthenticateUser
import es.adevinta.spain.friends.auth.JwtUtils
import es.adevinta.spain.friends.domain.contracts.FriendshipRepository
import es.adevinta.spain.friends.domain.contracts.UserRepository
import es.adevinta.spain.friends.services.PasswordEncoderService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.crypto.password.PasswordEncoder

@Configuration
class ApplicationConfiguration {

  @Bean
  fun registerUser(
    userRepository : UserRepository,
    passwordEncoderService: PasswordEncoderService
  ) = RegisterUser(userRepository, passwordEncoderService)

  @Bean
  fun authenticateUser(userRepository: UserRepository,
                       authenticationManager: AuthenticationManager,
                       jwtUtils: JwtUtils,
  ) = AuthenticateUser(userRepository,authenticationManager,jwtUtils)

  @Bean
  fun friendshipHandler(
    friendshipRepository: FriendshipRepository,
    userRepository: UserRepository) = FriendshipHandler(friendshipRepository, userRepository)

  @Bean
  fun getUser(userRepository : UserRepository) = GetUsers(userRepository)

  @Bean
  fun passwordEncoderService(passwordEncoder: PasswordEncoder) = PasswordEncoderService(passwordEncoder)
}
