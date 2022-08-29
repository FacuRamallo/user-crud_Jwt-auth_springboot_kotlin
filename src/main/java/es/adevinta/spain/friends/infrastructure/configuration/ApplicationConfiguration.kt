package es.adevinta.spain.friends.infrastructure.configuration

import es.adevinta.spain.friends.application.auth.RegisterUser
import es.adevinta.spain.friends.application.FriendshipHandler
import es.adevinta.spain.friends.application.GetUsers
import es.adevinta.spain.friends.application.auth.AuthenticateUser
import es.adevinta.spain.friends.auth.JwtUtils
import es.adevinta.spain.friends.domain.contracts.FriendshipRepository
import es.adevinta.spain.friends.domain.contracts.UserRepository
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.authentication.AuthenticationManager

@Configuration
class ApplicationConfiguration {

  @Bean
  fun registerUser(userRepository : UserRepository) = RegisterUser(userRepository)

  @Bean
  fun authenticateUser(userRepository: UserRepository,
                       authenticationManager: AuthenticationManager,
                       jwtUtils: JwtUtils
  ) = AuthenticateUser(userRepository,authenticationManager,jwtUtils)

  @Bean
  fun friendshipHandler(
    friendshipRepository: FriendshipRepository,
    userRepository: UserRepository) = FriendshipHandler(friendshipRepository, userRepository)

  @Bean
  fun getUser(userRepository : UserRepository) = GetUsers(userRepository)

}
