package es.adevinta.spain.friends.infrastructure.controller.dtos

import com.fasterxml.jackson.annotation.JsonProperty

data class LoggedUserDto(@JsonProperty("userName") val userName: String)
