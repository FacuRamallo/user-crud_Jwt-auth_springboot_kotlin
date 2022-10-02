package es.adevinta.spain.friends.integrationTests.acceptance

import es.adevinta.spain.friends.domain.Role.ROLE_USER
import es.adevinta.spain.friends.domain.User
import es.adevinta.spain.friends.domain.UserName
import es.adevinta.spain.friends.integrationTests.IntegrationTest
import io.restassured.module.mockmvc.RestAssuredMockMvc.given
import org.assertj.core.api.Assertions.contentOf
import org.hamcrest.Matchers.equalTo
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Value
import org.springframework.core.io.Resource
import org.springframework.http.HttpStatus.OK
import org.springframework.security.test.context.support.WithUserDetails

class FriendshipFeatureHandler: IntegrationTest() {

  private val user2 = User(UserName("user002"), "123456789", setOf(ROLE_USER))

  @Value("classpath:json/friendshipReqJson.json")
  private lateinit var newFriendshipReqDto: Resource

  @Value("classpath:apiResponses/Ok203response.json")
  private lateinit var ok203Response: Resource

  @Test
  @WithUserDetails("user001")
  fun `users can request friendship to another users`(){

    createTestUser(user2)

    given()
      .contentType("application/json")
      .body(newFriendshipReqDto.file)
      .post("v1/friendship")
      .then()
      .status(OK)
      .contentType("application/json")
      .body(equalTo(contentOf(ok203Response.file)))

  }

  fun createTestUser(user : User) {

    try {
      userRepository.add(user)
    }catch(e: Exception) {
      println(e.message + e.toString())
    }
  }
}
