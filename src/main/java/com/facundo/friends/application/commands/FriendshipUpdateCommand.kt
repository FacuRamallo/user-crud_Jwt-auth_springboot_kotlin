package com.facundo.friends.application.commands

data class FriendshipUpdateCommand(
  val requestedFrom: String,
  val requestStatus: String
)

