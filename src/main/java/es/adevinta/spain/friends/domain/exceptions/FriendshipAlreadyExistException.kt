package es.adevinta.spain.friends.domain.exceptions

import es.adevinta.spain.friends.domain.UserName

class FriendshipAlreadyExistException(target: UserName) : FriendshipException("Friendship already requested to ${target.value}")
