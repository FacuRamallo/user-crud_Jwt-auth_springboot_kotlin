package com.facundo.friends.application.auth.dtos

data class AuthUserDto(
  val token: String,
  val tokenType: String,
  val username: String,
  val roles: List<String>,
)
