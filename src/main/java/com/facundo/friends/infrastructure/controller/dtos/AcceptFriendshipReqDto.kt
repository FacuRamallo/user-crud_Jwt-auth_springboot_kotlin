package com.facundo.friends.infrastructure.controller.dtos

import com.fasterxml.jackson.annotation.JsonProperty

data class AcceptFriendshipReqDto(  @JsonProperty("requestedFrom") val requestedFrom: String)
