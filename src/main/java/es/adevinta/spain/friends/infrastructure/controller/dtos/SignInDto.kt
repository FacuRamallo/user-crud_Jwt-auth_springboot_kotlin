package es.adevinta.spain.friends.infrastructure.controller.dtos

import com.fasterxml.jackson.annotation.JsonProperty

data class SignInDto(
  @JsonProperty("UserName") val userName: String,
  @JsonProperty("Password") val password: String,
  @JsonProperty("Roles") val roles: Set<String>?,
)
