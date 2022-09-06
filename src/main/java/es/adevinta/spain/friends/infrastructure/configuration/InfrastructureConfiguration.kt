package es.adevinta.spain.friends.infrastructure.configuration

import com.fasterxml.jackson.databind.DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import es.adevinta.spain.friends.domain.contracts.FriendshipRepository
import es.adevinta.spain.friends.domain.contracts.UserRepository
import es.adevinta.spain.friends.infrastructure.auth.JwtUtils
import es.adevinta.spain.friends.infrastructure.auth.services.UserAuthenticationServiceImpl
import es.adevinta.spain.friends.infrastructure.repository.MapFriendshipRepository
import es.adevinta.spain.friends.infrastructure.repository.PostgresSQLUserRepository
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.security.authentication.AuthenticationManager


@Configuration
class InfrastructureConfiguration {

  @Bean
  fun objectMapper(): ObjectMapper = jacksonObjectMapper().configure(FAIL_ON_UNKNOWN_PROPERTIES, false)
  @Bean
  fun userRepository(
    jdbcTemplate: NamedParameterJdbcTemplate,
    mapper: ObjectMapper
  ) : UserRepository = PostgresSQLUserRepository(jdbcTemplate, mapper)

  @Bean
  fun friendshipRepository() : FriendshipRepository = MapFriendshipRepository()

  @Bean
  fun iUserAuthenticationService(authenticationManager: AuthenticationManager,jwtUtils: JwtUtils)  = UserAuthenticationServiceImpl(authenticationManager,  jwtUtils)
}
