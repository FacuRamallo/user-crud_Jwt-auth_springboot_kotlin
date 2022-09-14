package es.adevinta.spain.friends.integrationTests.acceptance


import es.adevinta.spain.friends.domain.PassWord
import es.adevinta.spain.friends.domain.Role
import es.adevinta.spain.friends.domain.Role.ROLE_ADMIN
import es.adevinta.spain.friends.domain.Role.ROLE_USER
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
    createTestUser("user001", "123456789",null)

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

  @Test
  fun `should return list of roles with ADMIN role`(){
    createTestUser("user001", "123456789", setOf(ROLE_USER,ROLE_ADMIN))

    given()
      .contentType("application/json")
      .body(userDto.file)
      .post("v1/authenticate")
      .then()
      .status(OK)
      .contentType(JSON)
      .body("roles",Matchers.hasItem("[ROLE_USER,ROLE_ADMIN]"))
      .extract().response()
  }



  fun createTestUser(username: String, password: String, roles: Set<Role>?) {
    val encodedPassword = passwordEncoderService.encodePassword(PassWord(password))
    val testUser : User = if(roles.isNullOrEmpty()){
      User(UserName(username), encodedPassword)
    }else {
      User(UserName(username), encodedPassword, roles)
    }

    try {
      userRepository.add(testUser)
    }catch(e: Exception) {
      println(e.message + e.toString())
    }
  }
}
