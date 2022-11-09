package com.facundo.friends.application.auth.commands

data class NewUserCommand(
  val userName: String,
  val passWord: String,
  val roles: Set<String>?
)
