package es.adevinta.spain.friends.infrastructure.controller

import es.adevinta.spain.friends.application.FriendshipRequestCommand
import es.adevinta.spain.friends.application.FrienshipHandler
import es.adevinta.spain.friends.application.GetUsers
import es.adevinta.spain.friends.domain.exceptions.FriendshipAlreadyRequestedException
import es.adevinta.spain.friends.domain.exceptions.FriendshipException
import es.adevinta.spain.friends.domain.exceptions.InvalidPasswordException
import es.adevinta.spain.friends.domain.exceptions.InvalidUsernameException
import es.adevinta.spain.friends.domain.exceptions.NameAlreadyExistException
import es.adevinta.spain.friends.domain.exceptions.SelfFriendshipException
import es.adevinta.spain.friends.domain.exceptions.TargetUserNameNotFoundException
import es.adevinta.spain.friends.domain.exceptions.UserDetailsException
import es.adevinta.spain.friends.infrastructure.apiResponses.ApiResponses.ERROR_100
import es.adevinta.spain.friends.infrastructure.apiResponses.ApiResponses.ERROR_101
import es.adevinta.spain.friends.infrastructure.apiResponses.ApiResponses.ERROR_102
import es.adevinta.spain.friends.infrastructure.apiResponses.ApiResponses.ERROR_103
import es.adevinta.spain.friends.infrastructure.apiResponses.ApiResponses.ERROR_104
import es.adevinta.spain.friends.infrastructure.apiResponses.ApiResponses.OK_201
import es.adevinta.spain.friends.infrastructure.apiResponses.ApiResponses.OK_203
import es.adevinta.spain.friends.infrastructure.controller.dtos.FriendshipReqDto
import es.adevinta.spain.friends.infrastructure.controller.dtos.LoggedUserDto
import java.lang.Exception
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody

@Controller
class FiendshipController(val friendshipHandler: FrienshipHandler ) {

  @PostMapping("/v1/friendship")
  fun newFriendshipRequest(@RequestBody reqFriendTo: FriendshipReqDto): ResponseEntity<String> {
    val command = FriendshipRequestCommand( reqFriendTo.friendUserName )

    friendshipHandler.execute(command)

    return OK_203.response()
  }

  @GetMapping("/v1/friendship")
  fun getFriends(@RequestBody userName: LoggedUserDto): ResponseEntity<String> {


    return OK_201.response()

  }

  @ExceptionHandler(value = [FriendshipException::class])
  fun handleFriendshipException(e: FriendshipException): ResponseEntity<String> =
    when (e) {
      is TargetUserNameNotFoundException -> ERROR_104.friendshipErrorResponse(e)
      is SelfFriendshipException -> ERROR_104.friendshipErrorResponse(e)
      is FriendshipAlreadyRequestedException -> ERROR_104.friendshipErrorResponse(e)
      else -> ERROR_103.response()
    }

}
