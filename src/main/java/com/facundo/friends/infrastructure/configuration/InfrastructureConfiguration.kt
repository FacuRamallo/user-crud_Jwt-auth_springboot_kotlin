package com.facundo.friends.infrastructure.configuration

import com.fasterxml.jackson.databind.DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.facundo.friends.application.FrienshipHandler
import com.facundo.friends.domain.contracts.FriendshipRepository
import com.facundo.friends.domain.contracts.UserAuthenticationService
import com.facundo.friends.domain.contracts.UserRepository
import com.facundo.friends.infrastructure.auth.JwtUtils
import com.facundo.friends.infrastructure.auth.services.UserAuthenticationServiceImpl
import com.facundo.friends.infrastructure.repository.PostgreSQLFriendshipRepository
import com.facundo.friends.infrastructure.repository.PostgresSQLRolesMapper
import com.facundo.friends.infrastructure.repository.PostgresSQLUserRepository
import com.facundo.friends.infrastructure.repository.RolesMapper
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
    rolesMapper: RolesMapper
  ) : UserRepository = PostgresSQLUserRepository(jdbcTemplate, rolesMapper)

  @Bean
  fun friendshipRepository(jdbcTemplate: NamedParameterJdbcTemplate) = PostgreSQLFriendshipRepository(jdbcTemplate)

  @Bean
  fun rolesMapper( jdbcTemplate: NamedParameterJdbcTemplate): RolesMapper = PostgresSQLRolesMapper(jdbcTemplate)

  @Bean
  fun userAuthenticationService(authenticationManager: AuthenticationManager, jwtUtils: JwtUtils)
      = UserAuthenticationServiceImpl(authenticationManager,  jwtUtils)

  @Bean
  fun friendshipHandler(
    userRepository: UserRepository,
    friendshipRepository: FriendshipRepository,
    userAuthenticationService: UserAuthenticationService
  ) = FrienshipHandler(
      userRepository,
      friendshipRepository,
      userAuthenticationService
  )
}
