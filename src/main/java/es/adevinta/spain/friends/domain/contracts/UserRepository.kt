package es.adevinta.spain.friends.domain.contracts

import es.adevinta.spain.friends.domain.User
import es.adevinta.spain.friends.domain.UserName

interface UserRepository {
  fun exist(userName: UserName) : Boolean
  fun add(user: User)
  fun getAll(): List<User>
  fun getByUserName(username: String): User?
}
