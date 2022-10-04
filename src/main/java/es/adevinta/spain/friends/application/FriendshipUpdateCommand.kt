package es.adevinta.spain.friends.application

data class FriendshipUpdateCommand(
  val requestedFrom: String,
  val requestStatus: String
)

