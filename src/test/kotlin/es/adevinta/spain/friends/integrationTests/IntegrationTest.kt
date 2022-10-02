package es.adevinta.spain.friends.integrationTests

import com.github.tomakehurst.wiremock.WireMockServer
import com.github.tomakehurst.wiremock.common.ConsoleNotifier
import com.github.tomakehurst.wiremock.core.WireMockConfiguration.options
import es.adevinta.spain.friends.FriendsApplication
import es.adevinta.spain.friends.domain.PassWord
import es.adevinta.spain.friends.domain.Role
import es.adevinta.spain.friends.domain.Role.ROLE_ADMIN
import es.adevinta.spain.friends.domain.Role.ROLE_USER
import es.adevinta.spain.friends.domain.User
import es.adevinta.spain.friends.domain.UserName
import es.adevinta.spain.friends.domain.contracts.UserRepository
import es.adevinta.spain.friends.infrastructure.auth.services.PasswordEncoderServiceImpl
import es.adevinta.spain.friends.integrationTests.helper.UserRepositoryForTest
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
      wireMockServer.start()
    }
  }

  @BeforeEach
  fun setUp() {
    mvc = MockMvcBuilders
      .webAppContextSetup(context)
      .apply<DefaultMockMvcBuilder>(springSecurity())
      .build()
    mockMvc(mvc)
    wireMockServer.resetAll()
    userRepositoryForTest.truncate()
    createTestUser("Admin", "123456789", setOf(ROLE_USER, ROLE_ADMIN))
    createTestUser("user001", "123654789",null)
  }

  fun createTestUser(username: String, password: String, roles: Set<Role>?) {
    val encodedPassword = passwordEncoderService.encodePassword(PassWord(password))
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


