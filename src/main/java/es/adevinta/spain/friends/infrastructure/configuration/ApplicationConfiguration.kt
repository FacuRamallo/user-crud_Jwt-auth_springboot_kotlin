package es.adevinta.spain.friends.infrastructure.configuration

import es.adevinta.spain.friends.application.CreateUser
import es.adevinta.spain.friends.application.FriendshipHandler
import es.adevinta.spain.friends.application.GetUsers
import es.adevinta.spain.friends.domain.contracts.FriendshipRepository
import es.adevinta.spain.friends.domain.contracts.UserRepository
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class ApplicationConfiguration {

  @Bean
  fun createUser(userRepository : UserRepository) = CreateUser(userRepository)

  @Bean
  fun friendshipHandler(
    friendshipRepository: FriendshipRepository,
    userRepository: UserRepository) = FriendshipHandler(friendshipRepository, userRepository)

  @Bean
  fun getUser(userRepository : UserRepository) = GetUsers(userRepository)

}
