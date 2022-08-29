package es.adevinta.spain.friends.infraestructure.helper

import org.springframework.jdbc.core.namedparam.MapSqlParameterSource
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate

class UserRepositoryForTest(private val jdbcTemplate: NamedParameterJdbcTemplate) {

  fun truncate() {
    val truncateQuery = "truncate users cascade "
    jdbcTemplate.update(truncateQuery, MapSqlParameterSource())
  }

}
