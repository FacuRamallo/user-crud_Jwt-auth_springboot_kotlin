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
  private val jdbcTemplate: NamedParameterJdbcTemplate
) : FriendshipRepository {

  override fun existBetween(username1: UserName, username2: UserName): Boolean {
    val sql = """
      SELECT EXISTS(SELECT * FROM friendships
      WHERE requestFrom = :requestFrom
      AND requestTo = :requestTo
      AND (status = :pending OR status = :accepted))
    """
    val existFriendSqlParameterSource = MapSqlParameterSource()
    existFriendSqlParameterSource.addValue("requestFrom", username1.value)
    existFriendSqlParameterSource.addValue("requestTo", username2.value)
    existFriendSqlParameterSource.addValue("pending", FriendshipStatus.PENDING.name)
    existFriendSqlParameterSource.addValue("accepted", FriendshipStatus.ACCEPTED.name)

    try {
      return jdbcTemplate.queryForObject(sql, existFriendSqlParameterSource, Boolean::class.java)!!
    } catch (e: DataAccessException) {
      throw UserRepositoryException(
        "Error in axistFriendship method for friendship between user ${username1.value} and ${username2.value}", e
      )
    }
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
        "Error in newFriendship method for friendship between user ${requester.value} and ${target.value}", e
      )
    }
  }

  override fun getFriends(userName: UserName): List<User?> {
    TODO("Not yet implemented")
  }
}
