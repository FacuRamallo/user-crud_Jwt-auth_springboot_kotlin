package com.facundo.friends.infrastructure.controller.dtos

import com.fasterxml.jackson.annotation.JsonProperty

data class FriendshipReqDto( @JsonProperty("reqFriendTo") val friendUserName: String )
