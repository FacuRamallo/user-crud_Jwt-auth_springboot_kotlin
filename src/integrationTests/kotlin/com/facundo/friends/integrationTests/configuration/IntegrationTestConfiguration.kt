package com.facundo.friends.integrationTests.configuration

import es.adevinta.spain.friends.integrationTests.helper.UserRepositoryForTest
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate

@Configuration
class IntegrationTestConfiguration {

  @Bean
  fun userRepositoryForTest(namedParameterJdbcTemplate: NamedParameterJdbcTemplate) = UserRepositoryForTest(namedParameterJdbcTemplate)
}
