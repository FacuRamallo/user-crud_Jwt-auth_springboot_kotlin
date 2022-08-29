package es.adevinta.spain.friends.infraestructure.acceptance

import es.adevinta.spain.friends.domain.PassWord
import es.adevinta.spain.friends.domain.User
import es.adevinta.spain.friends.domain.UserName
import es.adevinta.spain.friends.infraestructure.IntegrationTest
import es.adevinta.spain.friends.infrastructure.apiResponses.ApiResponses
import es.adevinta.spain.friends.infrastructure.apiResponses.ApiResponses.ERROR_100
import es.adevinta.spain.friends.infrastructure.apiResponses.ApiResponses.ERROR_101
import es.adevinta.spain.friends.infrastructure.apiResponses.ApiResponses.ERROR_102
import es.adevinta.spain.friends.infrastructure.apiResponses.ApiResponses.OK_201
import es.adevinta.spain.friends.infrastructure.configuration.SecurityConfig
import io.restassured.http.ContentType.JSON
import io.restassured.http.ContentType.TEXT
import io.restassured.module.mockmvc.RestAssuredMockMvc.given
import kotlin.test.assertFalse
import kotlin.test.assertTrue
import org.assertj.core.api.Assertions.contentOf
import org.hamcrest.Matchers.containsString
import org.hamcrest.Matchers.equalTo
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.core.io.Resource
import org.springframework.http.HttpStatus.BAD_REQUEST
import org.springframework.http.HttpStatus.CREATED
import org.springframework.http.HttpStatus.OK
import org.springframework.http.HttpStatus.UNAUTHORIZED
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.test.context.support.WithAnonymousUser
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.test.context.ContextConfiguration

class RegisterUserFeature : IntegrationTest() {

  @Value("classpath:json/newUser.json")
  private lateinit var newUserDto: Resource

  @Value("classpath:json/newUserWithWrongName.json")
  private lateinit var newUserWrongNameDto: Resource

  @Value("classpath:json/newUserWithWrongPassword.json")
  private lateinit var newUserWrongPasswordDto: Resource

  @Test
  fun `should fail when username is invalid`(){

    given()
      .contentType("application/json")
      .body(newUserWrongNameDto.file)
      .post("v1/signup")
      .then()
      .status(BAD_REQUEST)
      .contentType(JSON)
      .body(equalTo(ERROR_100.response().body))


  }

  @Test
  fun `should fail when username already exist`(){
    createTestUser("user001","123456789")

    given()
      .contentType("application/json")
      .body(newUserDto.file)
      .post("v1/signup")
      .then()
      .status(BAD_REQUEST)
      .contentType(JSON)
      .body(equalTo(ERROR_102.response().body))


  }

  @Test
  fun `should fail when password is invalid`(){

    given()
      .contentType("application/json")
      .body(newUserWrongPasswordDto.file)
      .post("v1/signup")
      .then()
      .status(BAD_REQUEST)
      .contentType(JSON)
      .body(equalTo(ERROR_101.response().body))

  }

  @Test
  fun `should create new user`(){

    given()
      .contentType("application/json")
      .body(newUserDto.file)
      .post("v1/signup")
      .then()
      .status(CREATED)
      .contentType(JSON)
      .body(equalTo(OK_201.response().body))



    assertTrue{ userRepository.exist(UserName("user001")) }

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
