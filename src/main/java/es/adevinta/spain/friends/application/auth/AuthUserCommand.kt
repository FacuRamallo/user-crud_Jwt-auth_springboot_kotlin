package es.adevinta.spain.friends.application.auth

data class AuthUserCommand(
  val token: String,
  val username: String,
  val roles: List<String>
)
