package com.facundo.friends.infrastructure.controller

import com.facundo.friends.application.commands.FriendshipUpdateCommand
import com.facundo.friends.application.commands.NewFriendshipRequestCommand
import com.facundo.friends.application.FrienshipHandler
import com.facundo.friends.domain.exceptions.FriendshipAlreadyExistException
import com.facundo.friends.domain.exceptions.FriendshipException
import com.facundo.friends.domain.exceptions.SelfFriendshipException
import com.facundo.friends.domain.exceptions.UserNameNotFoundException
import com.facundo.friends.infrastructure.apiResponses.ApiResponses.ERROR_103
import com.facundo.friends.infrastructure.apiResponses.ApiResponses.ERROR_104
import com.facundo.friends.infrastructure.apiResponses.ApiResponses.OK_203
import com.facundo.friends.infrastructure.apiResponses.ApiResponses.OK_204
import com.facundo.friends.infrastructure.controller.dtos.FriendshipReqDto
import com.facundo.friends.infrastructure.controller.dtos.FriendshipUpdateReqDto
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping

@Controller
@RequestMapping("/v1/friendship")
class FriendshipController(val friendshipHandler: FrienshipHandler ) {

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

  @ExceptionHandler(value = [FriendshipException::class])
  fun handleFriendshipException(e: FriendshipException): ResponseEntity<String> =
    when (e) {
      is UserNameNotFoundException -> ERROR_104.friendshipErrorResponse(e)
      is SelfFriendshipException -> ERROR_104.friendshipErrorResponse(e)
      is FriendshipAlreadyExistException -> ERROR_104.friendshipErrorResponse(e)
      else -> ERROR_103.response()
    }

}
