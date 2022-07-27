package es.adevinta.spain.friends.domain.contracts

import es.adevinta.spain.friends.domain.UserName
import es.adevinta.spain.friends.domain.Friend

interface FriendshipRepository {

  fun reqFriendship(userName: UserName, targetUserName: UserName)

  fun getFriends(userName: UserName) : List<Friend?>
}
