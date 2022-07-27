package es.adevinta.spain.friends.infrastructure.controller.dtos

import com.fasterxml.jackson.annotation.JsonProperty

data class FriendshipReqDto(
  @JsonProperty("loggedUser") val loggedUserName: String,
  @JsonProperty("reqFriendTo") val friendUserName: String,
)
