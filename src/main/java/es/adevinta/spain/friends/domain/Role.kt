package es.adevinta.spain.friends.domain

import kotlin.String

enum class Role(
  val roleName: String
) {
  ROLE_ADMIN("ROLE_ADMIN"),
  ROLE_USER("ROLE_USER");
}
