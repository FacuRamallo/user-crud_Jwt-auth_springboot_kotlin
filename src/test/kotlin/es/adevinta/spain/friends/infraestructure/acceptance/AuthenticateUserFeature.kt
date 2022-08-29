package es.adevinta.spain.friends.infraestructure.acceptance

import es.adevinta.spain.friends.domain.PassWord
import es.adevinta.spain.friends.domain.User
import es.adevinta.spain.friends.domain.UserName
import es.adevinta.spain.friends.infraestructure.IntegrationTest
import es.adevinta.spain.friends.infrastructure.apiResponses.ApiResponses.ERROR_100
import io.restassured.http.ContentType.JSON
import io.restassured.module.mockmvc.RestAssuredMockMvc.given
import org.hamcrest.Matchers
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Value
import org.springframework.core.io.Resource
import org.springframework.http.HttpStatus.BAD_REQUEST

class AuthenticateUserFeature : IntegrationTest() {

  @Value("classpath:json/newUser.json")
  private lateinit var userDto: Resource

  @Test
  fun `should authenticate user when login`(){
    createTestUser("user001", "123456789")

    given()
      .contentType("application/json")
      .body(userDto.file)
      .post("v1/singin")
      .then()
      .status(BAD_REQUEST)
      .contentType(JSON)
      .body(Matchers.equalTo(ERROR_100.response().body))
  }


  fun createTestUser(username: String, password: String) {

    val testUser = User(UserName(username), PassWord(password))
    try {
      userRepository.add(testUser)
    }catch(e: Exception) {
      println(e.message + e.toString())
    }
  }
}
