package com.facundo.friends.integrationTests

import com.github.tomakehurst.wiremock.WireMockServer
import com.github.tomakehurst.wiremock.common.ConsoleNotifier
import com.github.tomakehurst.wiremock.core.WireMockConfiguration.options
import com.facundo.friends.FriendsApplication
import com.facundo.friends.domain.PassWord
import com.facundo.friends.domain.Role
import com.facundo.friends.domain.Role.ROLE_ADMIN
import com.facundo.friends.domain.Role.ROLE_USER
import com.facundo.friends.domain.User
import com.facundo.friends.domain.UserName
import com.facundo.friends.domain.contracts.FriendshipRepository
import com.facundo.friends.domain.contracts.UserRepository
import com.facundo.friends.infrastructure.auth.services.PasswordEncoderServiceImpl
import com.facundo.friends.integrationTests.helper.UserRepositoryForTest
import io.restassured.module.mockmvc.RestAssuredMockMvc.mockMvc
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.core.env.Environment
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity
import org.springframework.test.annotation.DirtiesContext
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.setup.DefaultMockMvcBuilder
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.springframework.web.context.WebApplicationContext

@SpringBootTest(
  classes = [FriendsApplication::class]
)
@ExtendWith(SpringExtension::class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_CLASS)
@AutoConfigureMockMvc
class IntegrationTest {

  @Autowired
  private lateinit var context: WebApplicationContext

  private var mvc: MockMvc? = null

  @Autowired
  lateinit var env: Environment

  @Autowired
  lateinit var userRepository: UserRepository

  @Autowired
  lateinit var friendshipRepository: FriendshipRepository

  @Autowired
  lateinit var userRepositoryForTest: UserRepositoryForTest

  @Autowired
  lateinit var passwordEncoderService: PasswordEncoderServiceImpl

  companion object {
    val wireMockServer: WireMockServer = WireMockServer(
      options()
        .port(8888)
        .notifier(ConsoleNotifier(true))
    )

    @BeforeAll
    @JvmStatic
    fun setUpClass() {
      com.facundo.friends.integrationTests.IntegrationTest.Companion.wireMockServer.start()
    }
  }

  @BeforeEach
  fun setUp() {
    mvc = MockMvcBuilders
      .webAppContextSetup(context)
      .apply<DefaultMockMvcBuilder>(springSecurity())
      .build()
    mockMvc(mvc)
    com.facundo.friends.integrationTests.IntegrationTest.Companion.wireMockServer.resetAll()
    userRepositoryForTest.truncate()
    createTestUser("Admin", "123456789", setOf(ROLE_USER, ROLE_ADMIN))
    createTestUser("user001", "123654789",null)
  }

  fun createTestUser(username: String, password: String, roles: Set<Role>?) {
    val encodedPassword = passwordEncoderService.encodePassword(PassWord(password).value)
    val testUser : User = if(roles.isNullOrEmpty()){
      User(UserName(username), encodedPassword, emptySet())
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


