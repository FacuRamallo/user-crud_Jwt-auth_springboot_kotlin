package es.adevinta.spain.friends.application

import es.adevinta.spain.friends.domain.UserName
import es.adevinta.spain.friends.domain.contracts.FriendshipRepository
import es.adevinta.spain.friends.domain.contracts.UserAuthenticationService
import es.adevinta.spain.friends.domain.contracts.UserRepository
import es.adevinta.spain.friends.domain.exceptions.FriendshipAlreadyRequestedException
import es.adevinta.spain.friends.domain.exceptions.SelfFriendshipException
import es.adevinta.spain.friends.domain.exceptions.TargetUserNameNotFoundException


class FrienshipHandler(
  private val userRepository: UserRepository,
  private val friendshipRepository: FriendshipRepository,
  private val userAuthenticationService: UserAuthenticationService,
) {

  fun execute(command: FriendshipRequestCommand){
    val targetName = UserName( command.requestTo )
    val targetExist = userRepository.exist(targetName)
    val currentUserName = UserName( userAuthenticationService.getAuthenticatedUserName() )
    val isSelfFriendshipRequest = isRequestingFriendshipToHimself(targetName, currentUserName)
    val isFriendshipAlreadyRequested = isFriendshipAlreadyRequested( currentUserName, targetName )

    if (!targetExist) throw TargetUserNameNotFoundException(targetName)
    if (isSelfFriendshipRequest) throw SelfFriendshipException()
    if (isFriendshipAlreadyRequested) throw FriendshipAlreadyRequestedException(targetName)

    friendshipRepository.newFriendship(currentUserName,targetName)
  }

  private fun isRequestingFriendshipToHimself(target: UserName, current: UserName) = target.value == current.value

  private fun isFriendshipAlreadyRequested(from: UserName, to: UserName) = friendshipRepository.existBetween(from, to)
}
