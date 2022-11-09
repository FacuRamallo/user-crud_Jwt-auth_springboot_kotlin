package com.facundo.friends.integrationTests.acceptance

import com.facundo.friends.domain.Role.ROLE_ADMIN
import com.facundo.friends.domain.Role.ROLE_USER
import com.facundo.friends.domain.User
import com.facundo.friends.domain.UserName
import com.facundo.friends.infrastructure.apiResponses.ApiResponses.ERROR_102
import com.facundo.friends.infrastructure.apiResponses.ApiResponses.OK_201
import com.facundo.friends.integrationTests.IntegrationTest
import io.restassured.http.ContentType.JSON
import io.restassured.module.mockmvc.RestAssuredMockMvc.given
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import org.hamcrest.Matchers.equalTo
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Value
import org.springframework.core.io.Resource
import org.springframework.http.HttpStatus.BAD_REQUEST
import org.springframework.http.HttpStatus.CREATED

class RegisterUserFeature : IntegrationTest() {

  @Value("classpath:json/newUser.json")
  private lateinit var newUserDto: Resource

  @Value("classpath:json/newAdminUser.json")
  private lateinit var newAdminUserDto: Resource

  @Value("classpath:json/existingUser.json")
  private lateinit var existingUserDto: Resource

  @Test
  fun `should fail when username already exist`(){

    given()
      .contentType("application/json")
      .body(existingUserDto.file)
      .post("v1/signup")
      .then()
      .status(BAD_REQUEST)
      .contentType(JSON)
      .body(equalTo(ERROR_102.response().body))

  }

  @Test
  fun `should create new user with roles`(){

    given()
      .contentType("application/json")
      .body(newAdminUserDto.file)
      .post("v1/signup")
      .then()
      .status(CREATED)
      .contentType(JSON)
      .body(equalTo(OK_201.response().body))

    assertTrue{ userRepository.exist(UserName("Admin")) }

    val createdUserRoles = userRepository.getByUserName("Admin")?.roles

    assertEquals(setOf(ROLE_USER,ROLE_ADMIN),createdUserRoles)
  }

  @Test
  fun `should create user with ROLE_USER by default`(){
    given()
      .contentType("application/json")
      .body(newUserDto.file)
      .post("v1/signup")
      .then()
      .status(CREATED)
      .contentType(JSON)
      .body(equalTo(OK_201.response().body))

    assertTrue{ userRepository.exist(UserName("user002")) }

    val createdUserRoles = userRepository.getByUserName("user002")?.roles

    assertEquals(setOf(ROLE_USER),createdUserRoles)
  }

  fun createTestUser(username: String, password: String) {

    val testUser = User(UserName(username), password, emptySet())
    try {
      userRepository.add(testUser)
    }catch(e: Exception) {
      println(e.message + e.toString())
    }
  }
}
