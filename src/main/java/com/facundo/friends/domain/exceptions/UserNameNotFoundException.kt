package com.facundo.friends.domain.exceptions

class UserNameNotFoundException(val username: String) : FriendshipException("User $username not found")
