package es.adevinta.spain.friends.infrastructure.repository

import es.adevinta.spain.friends.domain.Friend
import es.adevinta.spain.friends.domain.Friendship
import es.adevinta.spain.friends.domain.FriendshipStatus
import es.adevinta.spain.friends.domain.FriendshipStatus.ACCEPTED
import es.adevinta.spain.friends.domain.UserName
import es.adevinta.spain.friends.domain.contracts.FriendshipRepository
import es.adevinta.spain.friends.domain.exceptions.UserRepositoryException
import java.sql.ResultSet
import java.time.LocalDateTime
import org.springframework.dao.DataAccessException
import org.springframework.jdbc.core.RowMapper
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate

class PostgreSQLFriendshipRepository(
  private val jdbcTemplate: NamedParameterJdbcTemplate
) : FriendshipRepository {

  override fun existBetween(requester: UserName, target: UserName): Boolean {
    val sql = """
      SELECT EXISTS(SELECT * FROM friendships
      WHERE (requestfrom,requestto) = (:requestFrom,:requestTo) OR (requestfrom,requestto) = (:requestTo,:requestFrom)
      AND (status = :pending OR status = :accepted))
    """
    val existFriendSqlParameterSource = MapSqlParameterSource()
    existFriendSqlParameterSource.addValue("requestFrom", requester.value)
    existFriendSqlParameterSource.addValue("requestTo", target.value)
    existFriendSqlParameterSource.addValue("pending", FriendshipStatus.PENDING.name)
    existFriendSqlParameterSource.addValue("accepted", ACCEPTED.name)

    try {
      return jdbcTemplate.queryForObject(sql, existFriendSqlParameterSource, Boolean::class.java)!!
    } catch (e: DataAccessException) {
      throw UserRepositoryException(
        "Error in axistFriendship method for friendship between user ${requester.value} and ${target.value}", e
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

  override fun updateStatus(requester: UserName, target: UserName, status: FriendshipStatus) {
    val sql = """
      UPDATE friendships
      SET status=:status, updated_at=:updatedAt
      WHERE  (requestFrom,requestTo) = (:requestFrom,:requestTo) OR (requestFrom,requestTo) = (:requestTo,:requestFrom)
    """

    val updateStatusSqlParameterSource = MapSqlParameterSource()
    updateStatusSqlParameterSource.addValue("requestFrom", requester.value)
    updateStatusSqlParameterSource.addValue("requestTo", target.value)
    updateStatusSqlParameterSource.addValue("status", status.name )
    updateStatusSqlParameterSource.addValue("updatedAt", LocalDateTime.now())

    try {
      jdbcTemplate.update(sql, updateStatusSqlParameterSource)
    } catch (e: DataAccessException) {
      throw UserRepositoryException(
        "Error in updateStatus method for friendship between user ${requester.value} and ${target.value}", e
      )
    }
  }

  override fun getFriendship(requester: UserName, target: UserName): Friendship? {
    val sql = """
      SELECT * FROM friendships
      WHERE (requestFrom,requestTo) = (:requestFrom,:requestTo) OR (requestFrom,requestTo) = (:requestTo,:requestFrom)
    """
    val getFriendshipSqlParameterSource = MapSqlParameterSource()
    getFriendshipSqlParameterSource.addValue("requestFrom", requester.value)
    getFriendshipSqlParameterSource.addValue("requestTo", target.value)

    try {
      return jdbcTemplate.queryForObject(sql, getFriendshipSqlParameterSource, mapToFriendship())
    } catch (e: DataAccessException) {
      throw UserRepositoryException(
        "Error in getFriendship method for friendship between user ${requester.value} and ${target.value}", e
      )
    }

  }

  override fun getFriends(userName: UserName): List<Friend>? {
    val sql = """
      SELECT * FROM friendships
      WHERE (requestFrom = :userName OR requestTo =:userName) AND status =:accepted
    """
    val getFriendsSqlParameterSource = MapSqlParameterSource()
    getFriendsSqlParameterSource.addValue("userName", userName.value)
    getFriendsSqlParameterSource.addValue("accepted", ACCEPTED.name)

    try {
      return jdbcTemplate.query(sql, getFriendsSqlParameterSource, mapToFriend(userName)).toList()
    } catch (e: DataAccessException) {
      throw UserRepositoryException(
        "Error in getFriends method for user ${userName.value}. ", e
      )
    }
  }

  private fun mapToFriendship() = RowMapper { rs: ResultSet, _: Int ->
    var endedBy : UserName? = null

    if( rs.getString("endedby") != null ) {endedBy = UserName(rs.getString("endedBy"))}

    Friendship(
      UserName(rs.getString("requestFrom")),
      UserName(rs.getString("requestTo")),
      FriendshipStatus.valueOf(rs.getString("status")),
      endedBy
      )
  }

  private fun mapToFriend(userName: UserName) = RowMapper { rs: ResultSet, _: Int ->
    var friendName = rs.getString("requestFrom")
    if (userName.value == friendName) { friendName = rs.getString("requestTo")}

    Friend(
      UserName(friendName),
      FriendshipStatus.valueOf(rs.getString("status"))
    )
  }


}
