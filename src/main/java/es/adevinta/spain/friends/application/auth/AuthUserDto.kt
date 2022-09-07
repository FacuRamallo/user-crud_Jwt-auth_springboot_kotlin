package es.adevinta.spain.friends.application.auth

data class AuthUserDto(
  val token: String,
  val tokenType: String,
  val username: String,
  val roles: List<String>,
)
