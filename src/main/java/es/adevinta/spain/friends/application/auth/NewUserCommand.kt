package es.adevinta.spain.friends.application.auth

data class NewUserCommand(
  val userName: String,
  val passWord: String,
  val roles: Set<String>?
)
