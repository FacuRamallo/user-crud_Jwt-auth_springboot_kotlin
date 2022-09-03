package es.adevinta.spain.friends.infrastructure.repository

import es.adevinta.spain.friends.domain.contracts.UserRepository
import es.adevinta.spain.friends.domain.PassWord
import es.adevinta.spain.friends.domain.User
import es.adevinta.spain.friends.domain.UserName
import es.adevinta.spain.friends.domain.exceptions.UserRepositoryException


class MapUserRepository(
  private val userTable : LinkedHashMap<kotlin.String, kotlin.String> = LinkedHashMap()
) : UserRepository {

  override fun exist(userName: UserName): Boolean {
    return userTable.containsKey(userName.value)
  }

  override fun add(user: User) {
    try {
      userTable.put(user.username.value, user.password)
    } catch (e: Throwable) {
      throw UserRepositoryException("Error creating user ${user.username.value}", e)
    }
  }

  override fun getAll(): List<User> {
    return userTable.map { User(UserName(it.key), it.value) }
  }

  override fun getByUserName(username: kotlin.String?): User? {
    TODO("Not yet implemented")
  }
}
