package com.facundo.friends.infrastructure.controller.dtos


data class LoggedUserDto(
  val token: String,
  val tokenType: String,
  val username: String,
  val roles: List<String>
  )
