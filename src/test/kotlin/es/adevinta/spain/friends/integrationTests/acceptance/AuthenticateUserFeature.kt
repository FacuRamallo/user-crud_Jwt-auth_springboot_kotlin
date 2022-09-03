package es.adevinta.spain.friends.integrationTests.acceptance


import es.adevinta.spain.friends.domain.PassWord
import es.adevinta.spain.friends.domain.User
import es.adevinta.spain.friends.domain.UserName
import es.adevinta.spain.friends.integrationTests.IntegrationTest
import es.adevinta.spain.friends.services.PasswordEncoderService
import io.restassured.http.ContentType.JSON
import io.restassured.module.mockmvc.RestAssuredMockMvc.given
import org.hamcrest.Matchers
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.core.io.Resource
import org.springframework.http.HttpStatus.OK

class AuthenticateUserFeature : IntegrationTest() {

  @Value("classpath:json/newUser.json")
  private lateinit var userDto: Resource

  @Autowired
  lateinit var passwordEncoderService: PasswordEncoderService

  @Test
  fun `should authenticate user when login`(){
    createTestUser("user001", "123456789")

    given()
      .contentType("application/json")
      .body(userDto.file)
      .post("v1/authenticate")
      .then()
      .status(OK)
      .contentType(JSON)
      .body(Matchers.containsString("\"Message\":\"User authenticated\""))
  }


  fun createTestUser(username: String, password: String) {
    val encodedPassword = passwordEncoderService.encodePassword(PassWord(password))
    val testUser = User(UserName(username), encodedPassword)
    try {
      userRepository.add(testUser)
    }catch(e: Exception) {
      println(e.message + e.toString())
    }
  }
}
