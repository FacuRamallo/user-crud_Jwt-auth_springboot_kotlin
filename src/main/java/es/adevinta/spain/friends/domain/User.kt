package es.adevinta.spain.friends.domain

import es.adevinta.spain.friends.domain.Role.ROLE_USER

class User(
  val username: UserName,
  val password: PassWord,
  private var roles: Set<Role> = setOf(ROLE_USER)
){
  fun getRoles(): Set<Role> = roles
}
