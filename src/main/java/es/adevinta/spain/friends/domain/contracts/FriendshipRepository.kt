package es.adevinta.spain.friends.domain.contracts

import es.adevinta.spain.friends.domain.UserName
import es.adevinta.spain.friends.domain.Friend
import es.adevinta.spain.friends.domain.User

interface FriendshipRepository {

  fun existBetween(username1 : UserName , username2 : UserName ) : Boolean

  fun newFriendship(requester: UserName, target: UserName)

  fun getFriends(userName: UserName) : List<User?>
}
