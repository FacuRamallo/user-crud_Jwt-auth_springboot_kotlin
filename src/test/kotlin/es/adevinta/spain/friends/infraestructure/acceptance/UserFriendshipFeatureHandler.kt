package es.adevinta.spain.friends.infraestructure.acceptance

import es.adevinta.spain.friends.domain.PassWord
import es.adevinta.spain.friends.domain.User
import es.adevinta.spain.friends.domain.UserName
import es.adevinta.spain.friends.infraestructure.IntegrationTest
import io.restassured.module.mockmvc.RestAssuredMockMvc.given
import kotlin.test.assertEquals
import org.assertj.core.api.Assertions.contentOf
import org.hamcrest.Matchers.equalTo
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Value
import org.springframework.core.io.Resource
import org.springframework.http.HttpStatus.OK

class UserFriendshipFeatureHandler: IntegrationTest() {

  private val loggedInUser1 = User(UserName("user01"),PassWord("1234567899"))
  private val user2 = User(UserName("user02"),PassWord("123456789"))

  @Value("classpath:json/friendshipReqJson.json")
  private lateinit var newFriendshipReqDto: Resource

  @Value("classpath:apiResponses/Ok203response.json")
  private lateinit var ok203Response: Resource

  @Test
  fun `users can request friendship to another users`(){

    userRepository.add(loggedInUser1)
    userRepository.add(user2)

    given()
      .contentType("application/json")
      .body(newFriendshipReqDto.file)
      .post("v1/friendship")
      .then()
      .status(OK)
      .contentType("application/json")
      .body(equalTo(contentOf(ok203Response.file)))

    assertEquals(1, friendshipRepository.getFriends(loggedInUser1.username).size)
  }
}
