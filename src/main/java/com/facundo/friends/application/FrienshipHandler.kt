package com.facundo.friends.application

import com.facundo.friends.application.commands.FriendshipUpdateCommand
import com.facundo.friends.application.commands.NewFriendshipRequestCommand
import com.facundo.friends.domain.FriendshipStatus
import com.facundo.friends.domain.UserName
import com.facundo.friends.domain.contracts.FriendshipRepository
import com.facundo.friends.domain.contracts.UserAuthenticationService
import com.facundo.friends.domain.contracts.UserRepository
import com.facundo.friends.domain.exceptions.FriendshipAlreadyExistException
import com.facundo.friends.domain.exceptions.FriendshipNotFoundException
import com.facundo.friends.domain.exceptions.SelfFriendshipException
import com.facundo.friends.domain.exceptions.UserNameNotFoundException


class FrienshipHandler(
  private val userRepository: UserRepository,
  private val friendshipRepository: FriendshipRepository,
  private val userAuthenticationService: UserAuthenticationService,
) {

  fun execute(command: NewFriendshipRequestCommand){
    val targetName = UserName(command.requestTo)
    val targetExist = userRepository.exist(targetName)
    val currentUserName = UserName(userAuthenticationService.getAuthenticatedUserName())
    val isSelfFriendshipRequest = isRequestingFriendshipToHimself(targetName, currentUserName)
    val isFriendshipAlreadyRequested = friendshipAlreadyExistBetween(currentUserName, targetName)

    if (!targetExist) throw UserNameNotFoundException(targetName.value)
    if (isSelfFriendshipRequest) throw SelfFriendshipException()
    if (isFriendshipAlreadyRequested) throw FriendshipAlreadyExistException(targetName)

    friendshipRepository.newFriendship(currentUserName,targetName)
  }

  fun execute(command: FriendshipUpdateCommand){
    val currentUserName = UserName(userAuthenticationService.getAuthenticatedUserName())
    val friendName = UserName(command.requestedFrom)
    val friendshipExist = friendshipAlreadyExistBetween(currentUserName, friendName)
    val newFriendshipStatus = FriendshipStatus.valueOf(command.requestStatus)

    if (!friendshipExist) throw FriendshipNotFoundException()

    friendshipRepository.updateStatus(currentUserName, friendName, newFriendshipStatus)
  }

  private fun isRequestingFriendshipToHimself(target: UserName, current: UserName) = target.value == current.value

  private fun friendshipAlreadyExistBetween(user1: UserName, user2: UserName) = friendshipRepository.existBetween(user1, user2)
}
