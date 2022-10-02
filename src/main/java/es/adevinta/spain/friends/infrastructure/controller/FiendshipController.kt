package es.adevinta.spain.friends.infrastructure.controller

import es.adevinta.spain.friends.application.FriendshipRequestCommand
import es.adevinta.spain.friends.application.FrienshipHandler
import es.adevinta.spain.friends.application.GetUsers
import es.adevinta.spain.friends.infrastructure.apiResponses.ApiResponses.OK_201
import es.adevinta.spain.friends.infrastructure.apiResponses.ApiResponses.OK_203
import es.adevinta.spain.friends.infrastructure.controller.dtos.FriendshipReqDto
import es.adevinta.spain.friends.infrastructure.controller.dtos.LoggedUserDto
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
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

}
