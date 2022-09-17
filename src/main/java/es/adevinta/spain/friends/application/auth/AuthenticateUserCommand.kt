package es.adevinta.spain.friends.application.auth

data class AuthenticateUserCommand(
  val userName: String,
  val passWord: String,
)
