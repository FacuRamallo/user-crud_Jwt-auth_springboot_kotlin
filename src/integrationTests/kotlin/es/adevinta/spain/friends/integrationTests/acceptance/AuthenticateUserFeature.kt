package es.adevinta.spain.friends.integrationTests.acceptance


import es.adevinta.spain.friends.integrationTests.IntegrationTest
import io.restassured.http.ContentType.JSON
import io.restassured.module.mockmvc.RestAssuredMockMvc.given
import org.hamcrest.Matchers.containsString
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Value
import org.springframework.core.io.Resource
import org.springframework.http.HttpStatus.CREATED
import org.springframework.http.HttpStatus.OK
import org.springframework.http.HttpStatus.UNAUTHORIZED
import org.springframework.security.test.context.support.WithUserDetails

class AuthenticateUserFeature : IntegrationTest() {

  @Value("classpath:json/existingUser.json")
  private lateinit var existingUserDto: Resource


  @Value("classpath:json/AdminUser.json")
  private lateinit var adminUserDto: Resource


  @Value("classpath:json/NotExistUser.json")
  private lateinit var notExistUserDto: Resource

  @Test
  fun `should authenticate user when login`(){

    given()
      .contentType("application/json")
      .body(existingUserDto.file)
      .post("v1/authenticate")
      .then()
      .status(OK)
      .contentType(JSON)
      .body(containsString("\"Message\":\"User authenticated\""))
  }

  @Test
  fun `should not authenticate user when login if it doesn't exist`(){

    given()
      .contentType("application/json")
      .body(notExistUserDto.file)
      .post("v1/authenticate")
      .then()
      .status(UNAUTHORIZED)

  }

  @Test
  fun `should return list of roles with ADMIN role`(){

    given()
      .contentType("application/json")
      .body(adminUserDto.file)
      .post("v1/authenticate")
      .then()
      .status(OK)
      .contentType(JSON)
      .body("roles", containsString(listOf("ROLE_USER","ROLE_ADMIN").toString()))
  }

  @Test
  @WithUserDetails("Admin")
  fun `should return the current user name`(){
    given()
      .post("v1/authenticatedUser")
      .then()
      .status(CREATED)
      .contentType(JSON)
      .body(containsString("\"CurrentUserName\":\"Admin\""))
  }
}
