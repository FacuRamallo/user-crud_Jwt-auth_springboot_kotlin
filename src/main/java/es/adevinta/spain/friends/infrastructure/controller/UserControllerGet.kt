package es.adevinta.spain.friends.infrastructure.controller

import es.adevinta.spain.friends.application.FriendshipHandler
import es.adevinta.spain.friends.application.GetUsers
import es.adevinta.spain.friends.domain.Friend
import es.adevinta.spain.friends.infrastructure.apiResponses.ApiResponses.OK_201
import es.adevinta.spain.friends.infrastructure.controller.dtos.LoggedUserDto
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestBody

@Controller
class UserControllerGet(val getUsers: GetUsers, val friendshipHandler: FriendshipHandler) {

  @GetMapping("/v1/friendship")
  fun getFriends(@RequestBody userName: LoggedUserDto): ResponseEntity<String> {
    val friends : List<Friend?> = friendshipHandler.getAllFriendsOf(userName)

    return OK_201.friendsResponse(friends)

  }
}