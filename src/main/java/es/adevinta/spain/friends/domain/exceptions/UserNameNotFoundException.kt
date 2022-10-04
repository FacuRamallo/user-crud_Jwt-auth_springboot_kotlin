package es.adevinta.spain.friends.domain.exceptions

import es.adevinta.spain.friends.domain.UserName

class UserNameNotFoundException(val username: String) : FriendshipException("User $username not found")
