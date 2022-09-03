package es.adevinta.spain.friends.domain

import kotlin.String

enum class Role(
  private var roleName: String
) {
  ROLE_ADMIN("ROLE_ADMIN"),
  ROLE_USER("ROLE_USER");

  fun getRoleName() : String = roleName
}