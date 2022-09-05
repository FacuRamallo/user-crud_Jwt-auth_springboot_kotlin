package es.adevinta.spain.friends.integrationTests.acceptance


import es.adevinta.spain.friends.domain.PassWord
import es.adevinta.spain.friends.domain.User
import es.adevinta.spain.friends.domain.UserName
import es.adevinta.spain.friends.integrationTests.IntegrationTest
import es.adevinta.spain.friends.infrastructure.auth.services.PasswordEncoderServiceImpl
import io.restassured.http.ContentType.JSON
import io.restassured.module.mockmvc.RestAssuredMockMvc.given
import org.hamcrest.Matchers
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.core.io.Resource
import org.springframework.http.HttpStatus.OK
import org.springframework.http.HttpStatus.UNAUTHORIZED

class AuthenticateUserFeature : IntegrationTest() {

  @Value("classpath:json/newUser.json")
  private lateinit var userDto: Resource

  @Autowired
  lateinit var passwordEncoderService: PasswordEncoderServiceImpl

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

  @Test
  fun `should not authenticate user when login if it doesn't exist`(){

    given()
      .contentType("application/json")
      .body(userDto.file)
      .post("v1/authenticate")
      .then()
      .status(UNAUTHORIZED)

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
