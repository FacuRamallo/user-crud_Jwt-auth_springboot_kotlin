package com.facundo.friends.integrationTests.acceptance

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.facundo.friends.domain.FriendshipStatus.ACCEPTED
import com.facundo.friends.domain.FriendshipStatus.CANCELED
import com.facundo.friends.domain.FriendshipStatus.PENDING
import com.facundo.friends.domain.Role.ROLE_USER
import com.facundo.friends.domain.User
import com.facundo.friends.domain.UserName
import com.facundo.friends.domain.exceptions.FriendshipAlreadyExistException
import com.facundo.friends.infrastructure.apiResponses.ApiResponses.ERROR_104
import com.facundo.friends.infrastructure.apiResponses.ApiResponses.OK_203
import com.facundo.friends.infrastructure.apiResponses.ApiResponses.OK_204
import com.facundo.friends.infrastructure.controller.dtos.FriendshipReqDto
import com.facundo.friends.infrastructure.controller.dtos.FriendshipUpdateReqDto
import com.facundo.friends.integrationTests.IntegrationTest
import io.restassured.module.mockmvc.RestAssuredMockMvc.given
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import org.hamcrest.Matchers.equalTo
import org.junit.jupiter.api.Test
import org.springframework.http.HttpStatus.BAD_REQUEST
import org.springframework.http.HttpStatus.OK
import org.springframework.security.test.context.support.WithUserDetails

class FriendshipHandlerFeature: IntegrationTest() {

  private val requester = User(UserName("user001"), "123654789",null)
  private val target = User(UserName("user002"), "123456789", setOf(ROLE_USER))
  private val friendshipRequestDto = FriendshipReqDto("user002")

  @Test
  @WithUserDetails("user001")
  fun `users can request friendship to another users`(){

    createTestUser(target)

    val jsonInput = jacksonObjectMapper().writeValueAsString(friendshipRequestDto)

    given()
      .contentType("application/json")
      .body(jsonInput)
      .post("v1/friendship")
      .then()
      .status(OK)
      .contentType("application/json")
      .body(equalTo(OK_203.response().body))

    assertTrue(friendshipRepository.existBetween(requester.username,target.username))

    val updatedFriendship = friendshipRepository.getFriendship(target.username,requester.username)

    assertEquals(PENDING.name, updatedFriendship?.status?.name)
  }

  @Test
  @WithUserDetails("user001")
  fun `A user cannot request friendship to a user that already has a pending or accepted request from him`(){
    createTestUser(target)

    createFriendship( requester, target )

    val jsonInput = jacksonObjectMapper().writeValueAsString(friendshipRequestDto)

    given()
      .contentType("application/json")
      .body(jsonInput)
      .post("v1/friendship")
      .then()
      .status(BAD_REQUEST)
      .contentType("application/json")
      .body(equalTo(ERROR_104.friendshipErrorResponse(FriendshipAlreadyExistException(target.username)).body))


  }

  @Test
  @WithUserDetails("user001")
  fun `A registered user can accepts requested friendship`(){
    createTestUser(target)

    createFriendship( target, requester )

    val acceptFriendshipReqDto = FriendshipUpdateReqDto(
      requestedFrom = "user002",
      requestStatus = "ACCEPTED"
    )

    val jsonInput = jacksonObjectMapper().writeValueAsString(acceptFriendshipReqDto)

    given()
      .contentType("application/json")
      .body(jsonInput)
      .put("v1/friendship")
      .then()
      .status(OK)
      .contentType("application/json")
      .body(equalTo(OK_204.response().body))

    val updatedFriendship = friendshipRepository.getFriendship(target.username,requester.username)

    assertEquals(ACCEPTED.name,updatedFriendship?.let { it.status.name })

  }


  @Test
  @WithUserDetails("user001")
  fun `a registered user can decline a requested friendship`() {
    createTestUser(target)

    createFriendship( target, requester )

    assertTrue(friendshipRepository.existBetween(target.username,requester.username))

    val declineFriendshipReqDto = FriendshipUpdateReqDto(
      requestedFrom = "user002",
      requestStatus = "CANCELED"
    )

    val jsonInput = jacksonObjectMapper().writeValueAsString(declineFriendshipReqDto)

    given()
      .contentType("application/json")
      .body(jsonInput)
      .put("v1/friendship")
      .then()
      .status(OK)
      .contentType("application/json")
      .body(equalTo(OK_204.response().body))

    val updatedFriendship = friendshipRepository.getFriendship(target.username,requester.username)

    assertEquals(CANCELED.name,updatedFriendship?.let { it.status.name })

  }

  fun createTestUser(user : User) {

    try {
      userRepository.add(user)
    }catch(e: Exception) {
      println(e.message + e.toString())
    }
  }

  fun createFriendship(requester: User , target: User){
    try {
      friendshipRepository.newFriendship(requester.username,target.username)
    }catch(e: Exception) {
      println(e.message + e.toString())
    }
  }
}
