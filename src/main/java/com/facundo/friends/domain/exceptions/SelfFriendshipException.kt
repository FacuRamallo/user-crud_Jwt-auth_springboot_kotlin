package com.facundo.friends.domain.exceptions

class SelfFriendshipException : FriendshipException("Friendship with yourself is forbidden")
