package es.adevinta.spain.friends.domain.contracts

import es.adevinta.spain.friends.domain.Friend
import es.adevinta.spain.friends.domain.UserName
import es.adevinta.spain.friends.domain.Friendship
import es.adevinta.spain.friends.domain.FriendshipStatus
import es.adevinta.spain.friends.domain.User

interface FriendshipRepository {

  fun existBetween(requester : UserName , target : UserName ) : Boolean

  fun newFriendship(requester: UserName, target: UserName)

  fun getFriends(userName: UserName) : List<Friend>?

  fun updateStatus(requester: UserName, target: UserName , status: FriendshipStatus)

  fun getFriendship( requester : UserName , target : UserName ) : Friendship?

}
