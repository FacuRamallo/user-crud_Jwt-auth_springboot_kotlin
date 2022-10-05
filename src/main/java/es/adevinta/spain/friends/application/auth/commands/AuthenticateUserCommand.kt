package es.adevinta.spain.friends.application.auth.commands

data class AuthenticateUserCommand(
  val userName: String,
  val passWord: String,
)
