package com.facundo.friends.domain

class Friendship(
  val requestFrom : UserName,
  val requestTo : UserName,
  val status: FriendshipStatus,
  val endedBy: UserName?,
)
