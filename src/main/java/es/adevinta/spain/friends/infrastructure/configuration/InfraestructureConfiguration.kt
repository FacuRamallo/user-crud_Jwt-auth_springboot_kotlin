package es.adevinta.spain.friends.infrastructure.configuration

import es.adevinta.spain.friends.domain.contracts.FriendshipRepository
import es.adevinta.spain.friends.domain.contracts.UserRepository
import es.adevinta.spain.friends.infrastructure.repository.MapFriendshipRepository
import es.adevinta.spain.friends.infrastructure.repository.MapUserRepository
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration


@Configuration
class InfraestructureConfiguration {

  @Bean
  fun userRepository() : UserRepository = MapUserRepository()

  @Bean
  fun friendshipRepository() : FriendshipRepository = MapFriendshipRepository()
}
