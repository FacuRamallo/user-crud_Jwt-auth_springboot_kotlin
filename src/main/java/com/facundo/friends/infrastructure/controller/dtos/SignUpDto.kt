package com.facundo.friends.infrastructure.controller.dtos

import com.fasterxml.jackson.annotation.JsonProperty

data class SignUpDto(
  @JsonProperty("UserName") val userName: String,
  @JsonProperty("Password") val password: String,
  @JsonProperty("Roles") val roles: Set<String>?,
)
