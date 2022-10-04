package es.adevinta.spain.friends.infrastructure.controller.dtos

import com.fasterxml.jackson.annotation.JsonProperty

data class AcceptFriendshipReqDto(  @JsonProperty("requestedFrom") val requestedFrom: String)
