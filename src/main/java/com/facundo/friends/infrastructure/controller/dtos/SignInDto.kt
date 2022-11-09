package com.facundo.friends.infrastructure.controller.dtos

import com.fasterxml.jackson.annotation.JsonProperty

data class SignInDto(
  @JsonProperty("UserName") val userName: String,
  @JsonProperty("Password") val password: String,
)
