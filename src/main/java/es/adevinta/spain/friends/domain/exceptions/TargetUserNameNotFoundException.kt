package es.adevinta.spain.friends.domain.exceptions

import es.adevinta.spain.friends.domain.UserName

class TargetUserNameNotFoundException(val username: UserName) : FriendshipException("User ${username.value} not found", )
