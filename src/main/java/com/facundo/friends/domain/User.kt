package com.facundo.friends.domain

class User(
  val username: UserName,
  val password: String,
  val roles: Set<Role>?
)
