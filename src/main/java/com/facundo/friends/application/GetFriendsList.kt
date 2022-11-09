package com.facundo.friends.application

import com.facundo.friends.application.dtos.FriendDto
import com.facundo.friends.domain.UserName
import com.facundo.friends.domain.contracts.FriendshipRepository
import com.facundo.friends.domain.contracts.UserAuthenticationService

open class GetFriendsList(
  private val friendshipRepository : FriendshipRepository,
  private val userAuthenticationService: UserAuthenticationService
  ) {
  open fun execute(): List<FriendDto>? {
    val currentUser = userAuthenticationService.getAuthenticatedUserName()
    val friendsList = friendshipRepository.getFriends(UserName(currentUser))
    return friendsList?.map { FriendDto(it.friendName.value) }
  }
}
