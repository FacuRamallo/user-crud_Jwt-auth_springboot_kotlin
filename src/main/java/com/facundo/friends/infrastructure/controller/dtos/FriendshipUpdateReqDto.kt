package com.facundo.friends.infrastructure.controller.dtos

import com.fasterxml.jackson.annotation.JsonProperty

data class FriendshipUpdateReqDto(
  @JsonProperty("requestedFrom")val requestedFrom: String,
  @JsonProperty("requestStatus")val requestStatus: String
)
