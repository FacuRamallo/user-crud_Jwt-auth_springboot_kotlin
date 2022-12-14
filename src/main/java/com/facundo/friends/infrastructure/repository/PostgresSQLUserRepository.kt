package com.facundo.friends.infrastructure.repository

import com.facundo.friends.domain.User
import com.facundo.friends.domain.UserName
import com.facundo.friends.domain.contracts.UserRepository
import com.facundo.friends.domain.exceptions.UserRepositoryException
import java.sql.ResultSet
import java.time.LocalDateTime.now
import org.springframework.dao.DataAccessException
import org.springframework.jdbc.core.RowMapper
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate

@Suppress("StringLiteralDuplication")
class PostgresSQLUserRepository(
  private val jdbcTemplate: NamedParameterJdbcTemplate,
  private val rolesMapper: RolesMapper
) : UserRepository {
  override fun exist(userName: UserName): Boolean {
    val query = """
      SELECT EXISTS(SELECT user_id FROM users
      WHERE username = :username)
    """
    val existMapSqlParameterSource = MapSqlParameterSource()
    existMapSqlParameterSource.addValue("username", userName.value)

    return try {
      jdbcTemplate.queryForObject(query, existMapSqlParameterSource, Boolean::class.java)!!
    } catch (e: DataAccessException) {
      throw UserRepositoryException(
        "Error in exists method for externalId: ${userName.value}", e
      )
    }

  }

  override fun add(user: User) {
    val sql = """
      INSERT INTO users (username, password, created_at, updated_at)
      VALUES (:username, :password, :created_at, :updated_at)
      """

    val addMapSqlParameterSource = MapSqlParameterSource()
    addMapSqlParameterSource.addValue("username", user.username.value)
    addMapSqlParameterSource.addValue("password", user.password)
    addMapSqlParameterSource.addValue("created_at", now())
    addMapSqlParameterSource.addValue("updated_at", now())

    try {
      jdbcTemplate.update(sql, addMapSqlParameterSource)
      rolesMapper.saveRoles(user.username.value,user.roles)
    } catch (e: DataAccessException) {
      throw UserRepositoryException(
        "Error in add method for user: ${user.username.value}", e
      )
    }
  }

  override fun getAll(): List<User> {
    TODO("Not yet implemented")
  }

  override fun getByUserName(username: String): User? {
    val query =  """
      SELECT username, password
      FROM users
      WHERE username = :username
    """

    val findByMapSqlParameterSource = MapSqlParameterSource()
    findByMapSqlParameterSource.addValue("username", username)

    try {
      return jdbcTemplate.queryForObject(query, findByMapSqlParameterSource, mapToUser())
    } catch (e: DataAccessException) {
      throw UserRepositoryException(
        "Error in getByUserName method for user: $username", e
      )
    }
  }

  private fun mapToUser() = RowMapper { rs: ResultSet, _: Int ->
    User(
      UserName(rs.getString("username")),
      rs.getString("password"),
      rolesMapper.getRoles(rs.getString("username"))
    )

  }

}
