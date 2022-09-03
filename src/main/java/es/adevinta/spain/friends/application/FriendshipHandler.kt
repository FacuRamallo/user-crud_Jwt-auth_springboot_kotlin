package es.adevinta.spain.friends.application

import es.adevinta.spain.friends.domain.UserName
import es.adevinta.spain.friends.domain.contracts.FriendshipRepository
import es.adevinta.spain.friends.domain.contracts.UserRepository
import es.adevinta.spain.friends.domain.exceptions.UserNameNotFoundException
import es.adevinta.spain.friends.infrastructure.controller.dtos.LoggedUserDto
import es.adevinta.spain.friends.domain.Friend

class FriendshipHandler(
  private val friendshipRepository: FriendshipRepository,
  private val userRepository: UserRepository)
{
  fun reqFriendship(reqUserName: String, newFriendName: String){

    val currentUser = UserName(reqUserName)
    if(!userRepository.exist(UserName(newFriendName))) throw UserNameNotFoundException(reqUserName)
    val friendUserName = UserName(newFriendName)

    friendshipRepository.reqFriendship(currentUser, friendUserName)
  }

  fun getAllFriendsOf(loggedUserName: LoggedUserDto) : List<Friend?> {
    val userName = UserName(loggedUserName.username)
    return friendshipRepository.getFriends(userName)
  }
}
