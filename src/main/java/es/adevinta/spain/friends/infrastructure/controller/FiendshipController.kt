package es.adevinta.spain.friends.infrastructure.controller

import es.adevinta.spain.friends.application.FriendshipUpdateCommand
import es.adevinta.spain.friends.application.NewFriendshipRequestCommand
import es.adevinta.spain.friends.application.FrienshipHandler
import es.adevinta.spain.friends.domain.exceptions.FriendshipAlreadyExistException
import es.adevinta.spain.friends.domain.exceptions.FriendshipException
import es.adevinta.spain.friends.domain.exceptions.SelfFriendshipException
import es.adevinta.spain.friends.domain.exceptions.UserNameNotFoundException
import es.adevinta.spain.friends.infrastructure.apiResponses.ApiResponses.ERROR_103
import es.adevinta.spain.friends.infrastructure.apiResponses.ApiResponses.ERROR_104
import es.adevinta.spain.friends.infrastructure.apiResponses.ApiResponses.OK_201
import es.adevinta.spain.friends.infrastructure.apiResponses.ApiResponses.OK_203
import es.adevinta.spain.friends.infrastructure.apiResponses.ApiResponses.OK_204
import es.adevinta.spain.friends.infrastructure.controller.dtos.FriendshipReqDto
import es.adevinta.spain.friends.infrastructure.controller.dtos.FriendshipUpdateReqDto
import es.adevinta.spain.friends.infrastructure.controller.dtos.LoggedUserDto
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping

@Controller
@RequestMapping("/v1/friendship")
class FiendshipController(val friendshipHandler: FrienshipHandler ) {

  @PostMapping
  fun newFriendshipRequest(@RequestBody reqFriendTo: FriendshipReqDto): ResponseEntity<String> {
    val command = NewFriendshipRequestCommand( reqFriendTo.friendUserName )

    friendshipHandler.execute(command)

    return OK_203.response()
  }

  @PutMapping
  fun updateFriendshipStatus(@RequestBody friendshipToUpdate : FriendshipUpdateReqDto): ResponseEntity<String> {
    val command = FriendshipUpdateCommand(
      friendshipToUpdate.requestedFrom,
      friendshipToUpdate.requestStatus
    )

    friendshipHandler.execute(command)

    return OK_204.response()

  }

  @GetMapping
  fun getFriends(@RequestBody userName: LoggedUserDto): ResponseEntity<String> {


    return OK_201.response()

  }

  @ExceptionHandler(value = [FriendshipException::class])
  fun handleFriendshipException(e: FriendshipException): ResponseEntity<String> =
    when (e) {
      is UserNameNotFoundException -> ERROR_104.friendshipErrorResponse(e)
      is SelfFriendshipException -> ERROR_104.friendshipErrorResponse(e)
      is FriendshipAlreadyExistException -> ERROR_104.friendshipErrorResponse(e)
      else -> ERROR_103.response()
    }

}
