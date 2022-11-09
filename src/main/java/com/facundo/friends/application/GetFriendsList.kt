package com.facundo.friends.application

import es.adevinta.spain.friends.application.dtos.FriendDto
import es.adevinta.spain.friends.domain.UserName
import es.adevinta.spain.friends.domain.contracts.FriendshipRepository
import es.adevinta.spain.friends.domain.contracts.UserAuthenticationService

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
