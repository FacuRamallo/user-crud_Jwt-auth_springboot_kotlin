package es.adevinta.spain.friends.domain

class Friendship(
  val requestFrom : UserName,
  val requestTo : UserName,
  val status: FriendshipStatus,
  val endedBy: UserName?,
)
