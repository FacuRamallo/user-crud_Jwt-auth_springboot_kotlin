package es.adevinta.spain.friends.application

import es.adevinta.spain.friends.application.commands.FriendshipUpdateCommand
import es.adevinta.spain.friends.application.commands.NewFriendshipRequestCommand
import es.adevinta.spain.friends.domain.FriendshipStatus
import es.adevinta.spain.friends.domain.UserName
import es.adevinta.spain.friends.domain.contracts.FriendshipRepository
import es.adevinta.spain.friends.domain.contracts.UserAuthenticationService
import es.adevinta.spain.friends.domain.contracts.UserRepository
import es.adevinta.spain.friends.domain.exceptions.FriendshipAlreadyExistException
import es.adevinta.spain.friends.domain.exceptions.FriendshipNotFoundException
import es.adevinta.spain.friends.domain.exceptions.SelfFriendshipException
import es.adevinta.spain.friends.domain.exceptions.UserNameNotFoundException


class FrienshipHandler(
  private val userRepository: UserRepository,
  private val friendshipRepository: FriendshipRepository,
  private val userAuthenticationService: UserAuthenticationService,
) {

  fun execute(command: NewFriendshipRequestCommand){
    val targetName = UserName( command.requestTo )
    val targetExist = userRepository.exist(targetName)
    val currentUserName = UserName( userAuthenticationService.getAuthenticatedUserName() )
    val isSelfFriendshipRequest = isRequestingFriendshipToHimself(targetName, currentUserName)
    val isFriendshipAlreadyRequested = friendshipAlreadyExistBetween( currentUserName, targetName )

    if (!targetExist) throw UserNameNotFoundException(targetName.value)
    if (isSelfFriendshipRequest) throw SelfFriendshipException()
    if (isFriendshipAlreadyRequested) throw FriendshipAlreadyExistException(targetName)

    friendshipRepository.newFriendship(currentUserName,targetName)
  }

  fun execute(command: FriendshipUpdateCommand){
    val currentUserName = UserName( userAuthenticationService.getAuthenticatedUserName() )
    val friendName = UserName( command.requestedFrom )
    val frienshipExist = friendshipAlreadyExistBetween(currentUserName,friendName)
    val newFriendshipStatus = FriendshipStatus.valueOf(command.requestStatus)

    if (!frienshipExist) throw FriendshipNotFoundException()

    friendshipRepository.updateStatus( currentUserName, friendName, newFriendshipStatus)
  }

  private fun isRequestingFriendshipToHimself(target: UserName, current: UserName) = target.value == current.value

  private fun friendshipAlreadyExistBetween(user1: UserName, user2: UserName) = friendshipRepository.existBetween(user1, user2)
}
