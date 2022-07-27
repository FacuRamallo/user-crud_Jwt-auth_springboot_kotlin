package es.adevinta.spain.friends.infrastructure.repository

import es.adevinta.spain.friends.domain.Friend
import es.adevinta.spain.friends.domain.UserName
import es.adevinta.spain.friends.domain.contracts.FriendshipRepository

class MapFriendshipRepository(private val friendshipTable : LinkedHashMap<String, Friend> = LinkedHashMap()) :
  FriendshipRepository {

  override fun getFriends(userName: UserName): List<Friend?> {
    return listOf(friendshipTable.get(userName.value))
  }

  override fun reqFriendship(userName: UserName, targetUserName: UserName) {
    friendshipTable.put(userName.value, Friend(targetUserName.value,false))
  }

}
