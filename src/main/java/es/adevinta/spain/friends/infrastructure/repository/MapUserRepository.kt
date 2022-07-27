package es.adevinta.spain.friends.infrastructure.repository

import es.adevinta.spain.friends.domain.contracts.UserRepository
import es.adevinta.spain.friends.domain.exceptions.MapRepositoryException
import es.adevinta.spain.friends.domain.PassWord
import es.adevinta.spain.friends.domain.User
import es.adevinta.spain.friends.domain.UserName


class MapUserRepository(
  private val userTable : LinkedHashMap<String,String> = LinkedHashMap()
) : UserRepository {

  override fun exist(userName: UserName): Boolean {
    return userTable.containsKey(userName.value)
  }

  override fun add(user: User) {
    try {
      userTable.put(user.username.value, user.password.value)
    } catch (e: Throwable) {
      throw MapRepositoryException("Error creating user ${user.username.value}", e)
    }
  }

  override fun getAll(): List<User> {
    return userTable.map { User(UserName(it.key), PassWord(it.value)) }
  }
}
