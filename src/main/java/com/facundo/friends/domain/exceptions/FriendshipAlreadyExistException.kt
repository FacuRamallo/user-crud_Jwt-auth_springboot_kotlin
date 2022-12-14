package com.facundo.friends.domain.exceptions

import com.facundo.friends.domain.UserName

class FriendshipAlreadyExistException(target: UserName) : FriendshipException("Friendship already requested to ${target.value}")
