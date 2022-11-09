package com.facundo.friends.domain.contracts

import com.facundo.friends.domain.User
import com.facundo.friends.domain.UserName

interface UserRepository {
  fun exist(userName: UserName) : Boolean
  fun add(user: User)
  fun getAll(): List<User>
  fun getByUserName(username: String): User?
}
