package es.adevinta.spain.friends.application

import es.adevinta.spain.friends.domain.UserName
import es.adevinta.spain.friends.domain.contracts.FriendshipRepository
import es.adevinta.spain.friends.domain.contracts.UserAuthenticationService
import es.adevinta.spain.friends.domain.contracts.UserRepository
import es.adevinta.spain.friends.domain.exceptions.SelfFriendshipException
import es.adevinta.spain.friends.domain.exceptions.TargetUserNameNotFoundException


class FrienshipHandler(
  private val userRepository: UserRepository,
  private val friendshipRepository: FriendshipRepository,
  private val userAuthenticationService: UserAuthenticationService,
) {

  fun execute(command: FriendshipRequestCommand){
    val target = command.requestTo
    val targetExist = userRepository.exist(UserName(target))
    val currentUser = userAuthenticationService.getAuthenticatedUserName()
    val isSelfFriendshipRequest = isRequestingFriendshipToHimself(target, currentUser)

    if (!targetExist) throw TargetUserNameNotFoundException(target)

    if (isSelfFriendshipRequest) throw SelfFriendshipException()


    friendshipRepository.newFriendship(UserName(currentUser),UserName(target))
  }

  private fun isRequestingFriendshipToHimself(target: String, current: String) = target == current
}
