package com.facundo.friends.domain.contracts

import com.facundo.friends.domain.Friend
import com.facundo.friends.domain.UserName
import com.facundo.friends.domain.Friendship
import com.facundo.friends.domain.FriendshipStatus

interface FriendshipRepository {

  fun existBetween(requester : UserName , target : UserName ) : Boolean

  fun newFriendship(requester: UserName, target: UserName)

  fun getFriends(userName: UserName) : List<Friend>?

  fun updateStatus(requester: UserName, target: UserName , status: FriendshipStatus)

  fun getFriendship( requester : UserName , target : UserName ) : Friendship?

}
