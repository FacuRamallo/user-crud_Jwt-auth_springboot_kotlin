package es.adevinta.spain.friends.infrastructure.repository

import es.adevinta.spain.friends.domain.Role
import java.sql.ResultSet
import org.springframework.jdbc.core.RowMapper
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate

@Suppress("StringLiteralDuplication")
class PostgresSQLRolesMapper(
  private val jdbcTemplate: NamedParameterJdbcTemplate
) : RolesMapper {

  override fun saveRoles(username: String, roles: Set<Role>?){
    val sql = """
      INSERT INTO users_roles (username, rolename)
      VALUES (:username, :rolename)
      """

    val saveMapSqlParameterSource = mutableListOf<MapSqlParameterSource>()

    roles?.forEach {
      val paramSource = MapSqlParameterSource()
      paramSource.addValue("username", username)
      paramSource.addValue("rolename",it.roleName)
      saveMapSqlParameterSource.add(paramSource)
    }

    saveMapSqlParameterSource.forEach {
      jdbcTemplate.update(sql,it)
    }
  }

 override fun getRoles(username: String): Set<Role> {
    val sql = """SELECT rolename FROM users_roles WHERE username = :username"""

    val roleMapSqlParameterSource = MapSqlParameterSource()

    roleMapSqlParameterSource.addValue("username", username)

    return jdbcTemplate.query(sql,roleMapSqlParameterSource, mapToRole()).toSet()

  }

  private fun mapToRole() = RowMapper { rs: ResultSet, _: Int ->
        Role.valueOf(rs.getString("rolename"))
  }

}
