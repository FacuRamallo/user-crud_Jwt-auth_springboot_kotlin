package es.adevinta.spain.friends.infrastructure.repository

import es.adevinta.spain.friends.domain.FriendshipStatus
import es.adevinta.spain.friends.domain.User
import es.adevinta.spain.friends.domain.UserName
import es.adevinta.spain.friends.domain.contracts.FriendshipRepository
import es.adevinta.spain.friends.domain.exceptions.UserRepositoryException
import java.time.LocalDateTime
import org.springframework.dao.DataAccessException
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate

class PostgreSQLFriendshipRepository(
  val jdbcTemplate: NamedParameterJdbcTemplate
) : FriendshipRepository {

  override fun existBetween(username1: UserName, username2: UserName): Boolean {
    TODO("Not yet implemented")
  }

  override fun newFriendship(requester: UserName, target: UserName) {
    val sql = """
      INSERT INTO friendships (requestFrom, requestTo , status, endedBy, created_at, updated_at)
      VALUES (:requestFrom, :requestTo , :status, :endedBy, :created_at, :updated_at)
      """

    val newFriendSqlParameterSource = MapSqlParameterSource()
    newFriendSqlParameterSource.addValue("requestFrom", requester.value)
    newFriendSqlParameterSource.addValue("requestTo", target.value)
    newFriendSqlParameterSource.addValue("status", FriendshipStatus.PENDING.name)
    newFriendSqlParameterSource.addValue("endedBy", null)
    newFriendSqlParameterSource.addValue("created_at", LocalDateTime.now())
    newFriendSqlParameterSource.addValue("updated_at", LocalDateTime.now())

    try {
      jdbcTemplate.update(sql, newFriendSqlParameterSource)
    } catch (e: DataAccessException) {
      throw UserRepositoryException(
        "Error in add method for friendship between user ${requester.value} and ${target.value}", e
      )
    }
  }

  override fun getFriends(userName: UserName): List<User?> {
    TODO("Not yet implemented")
  }
}
