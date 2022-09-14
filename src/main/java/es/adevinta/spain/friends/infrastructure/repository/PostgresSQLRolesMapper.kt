package es.adevinta.spain.friends.infrastructure.repository

import es.adevinta.spain.friends.domain.Role
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate

class PostgresSQLRolesMapper(
  private val jdbcTemplate: NamedParameterJdbcTemplate
) : RolesMapper {

  override fun saveRoles(username: String, roles: Set<Role>){
    val sql = """
      INSERT INTO users_roles (username, rolename)
      VALUES (:username, :rolename)
      """

    val saveMapSqlParameterSource = mutableListOf<MapSqlParameterSource>()

    roles.forEach {
      val paramSource = MapSqlParameterSource()
      paramSource.addValue("username", username)
      paramSource.addValue("rolename",it.getRoleName())
      saveMapSqlParameterSource.add(paramSource)
    }

    saveMapSqlParameterSource.forEach {
      jdbcTemplate.update(sql,it)
    }
  }

/*  fun getRoles(username: String): Set<Role> {
    val sql = """
      SELECT rolename
      FROM users_roles
      WHERE username = :username
      """
    val roleMapSqlParameterSource = MapSqlParameterSource()

    roleMapSqlParameterSource.addValue("username", username)

  }*/


}
