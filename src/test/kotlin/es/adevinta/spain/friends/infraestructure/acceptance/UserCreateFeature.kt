package es.adevinta.spain.friends.infraestructure.acceptance

import es.adevinta.spain.friends.domain.PassWord
import es.adevinta.spain.friends.domain.User
import es.adevinta.spain.friends.domain.UserName
import es.adevinta.spain.friends.infraestructure.IntegrationTest
import io.restassured.module.mockmvc.RestAssuredMockMvc.given
import kotlin.test.assertTrue
import org.assertj.core.api.Assertions.contentOf
import org.hamcrest.Matchers.equalTo
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Value
import org.springframework.core.io.Resource
import org.springframework.http.HttpStatus.CREATED
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.security.test.context.support.WithSecurityContext
import org.springframework.security.test.context.support.WithSecurityContextFactory
import org.springframework.security.test.context.support.WithUserDetails


class UserCreateFeature : IntegrationTest() {

  @Value("classpath:json/newUser.json")
  private lateinit var newUserDto: Resource

  @Value("classpath:apiResponses/Ok201response.json")
  private lateinit var ok201Response: Resource

  private var newUser = User(UserName("user04"), PassWord("qwer1234"))

  @Test
  fun `should create a new user`(){
    given()
      .contentType("application/json")
      .body(newUserDto.file)
      .post("v1/user")
      .then()
      .status(CREATED)
      .contentType("application/json")
      .body(equalTo(contentOf(ok201Response.file)))

    assertTrue { userRepository.exist(newUser.username) }
  }
}
