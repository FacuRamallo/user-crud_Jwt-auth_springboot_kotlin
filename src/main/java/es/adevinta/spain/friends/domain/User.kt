package es.adevinta.spain.friends.domain

import es.adevinta.spain.friends.domain.Role.ROLE_USER

class User(
  val username: UserName,
  val password: String,
  val roles: Set<Role>?
)
