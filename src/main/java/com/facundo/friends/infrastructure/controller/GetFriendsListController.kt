package com.facundo.friends.infrastructure.controller

import com.facundo.friends.application.GetFriendsList
import com.facundo.friends.infrastructure.apiResponses.ApiResponses.OK_204
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
