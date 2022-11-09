package com.facundo.friends.infrastructure.controller

import es.adevinta.spain.friends.application.GetFriendsList
import es.adevinta.spain.friends.infrastructure.apiResponses.ApiResponses.OK_204
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping

@Controller
class GetFriendsListController(
  val getFriendsList: GetFriendsList
) {

  @GetMapping("/v1/friends")
  fun getFriendsList(): ResponseEntity<String> {
    val friendsDtoList = getFriendsList.execute()

    return OK_204.friendsListResponse(friendsDtoList)
  }

}
