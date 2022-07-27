package es.adevinta.spain.friends.infrastructure.controller

import es.adevinta.spain.friends.application.CreateUser
import es.adevinta.spain.friends.application.FriendshipHandler
import es.adevinta.spain.friends.infrastructure.controller.dtos.FriendshipReqDto
import es.adevinta.spain.friends.infrastructure.apiResponses.ApiResponses.OK_201
import es.adevinta.spain.friends.infrastructure.controller.dtos.NewUserDto
import es.adevinta.spain.friends.infrastructure.apiResponses.ApiResponses.OK_203
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class UserControllerPost(val createUser: CreateUser, val friendshipHandler: FriendshipHandler) {

  @PostMapping("/v1/user")
  fun create(@RequestBody user: NewUserDto): ResponseEntity<String>{
    createUser.execute(user)

    return OK_201.response()
  }

  @PostMapping("/v1/friendship")
  fun reqFriendship(@RequestBody friendReq: FriendshipReqDto): ResponseEntity<String>{

    friendshipHandler.reqFriendship(friendReq.loggedUserName, friendReq.friendUserName)

    return OK_203.response()
  }

}
