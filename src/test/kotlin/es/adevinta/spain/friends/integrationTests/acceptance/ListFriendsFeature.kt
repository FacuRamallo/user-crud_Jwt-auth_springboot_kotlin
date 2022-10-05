package es.adevinta.spain.friends.integrationTests.acceptance

import es.adevinta.spain.friends.domain.Friend
import es.adevinta.spain.friends.domain.FriendshipStatus
import es.adevinta.spain.friends.domain.FriendshipStatus.ACCEPTED
import es.adevinta.spain.friends.domain.Role.ROLE_USER
import es.adevinta.spain.friends.domain.User
import es.adevinta.spain.friends.domain.UserName
import es.adevinta.spain.friends.infrastructure.apiResponses.ApiResponses.OK_203
import es.adevinta.spain.friends.infrastructure.apiResponses.ApiResponses.OK_205
import es.adevinta.spain.friends.integrationTests.IntegrationTest
import io.restassured.module.mockmvc.RestAssuredMockMvc.given
import org.hamcrest.Matchers
import org.hamcrest.Matchers.equalTo
import org.junit.jupiter.api.Test
import org.springframework.http.HttpStatus.OK
import org.springframework.security.test.context.support.WithUserDetails

class ListFriendsFeature : IntegrationTest() {



  @Test
  @WithUserDetails("user001")
  fun `should list friends of a registered user`(){
    val currUser = User(UserName("user001"), "123654789",setOf(ROLE_USER))
    val user003 = User(UserName("user003"),"qwert12345", setOf(ROLE_USER))
    val user004 = User(UserName("user004"),"qwert12345", setOf(ROLE_USER))
    val user005 = User(UserName("user005"),"qwert12345", setOf(ROLE_USER))
    createTestUser(user003)
    createTestUser(user004)
    createTestUser(user005)
    createFriendship(currUser,user003)
    createFriendship(currUser,user004)
    createFriendship(currUser,user005)
    updateFriendship(currUser.username,user003.username,ACCEPTED)
    updateFriendship(currUser.username,user004.username,ACCEPTED)
    updateFriendship(currUser.username,user005.username,ACCEPTED)

    val expectedFriendsList = listOf(
      Friend(user003.username,ACCEPTED),
      Friend(user004.username,ACCEPTED),
      Friend(user005.username,ACCEPTED)
    )


    given()
      .get("v1/friends")
      .then()
      .status(OK)
      .contentType("application/json")
      .body(equalTo(OK_205.friendsListResponse( expectedFriendsList ).body))

  }

  fun createTestUser(user : User) {

    try {
      userRepository.add(user)
    }catch(e: Exception) {
      println(e.message + e.toString())
    }
  }

  fun createFriendship(requester: User, target: User){
    try {
      friendshipRepository.newFriendship(requester.username,target.username)
    }catch(e: Exception) {
      println(e.message + e.toString())
    }
  }

  fun updateFriendship(requester: UserName, target: UserName, status: FriendshipStatus){
    try {
      friendshipRepository.updateStatus(requester,target, status)
    }catch(e: Exception) {
      println(e.message + e.toString())
    }
  }
}
