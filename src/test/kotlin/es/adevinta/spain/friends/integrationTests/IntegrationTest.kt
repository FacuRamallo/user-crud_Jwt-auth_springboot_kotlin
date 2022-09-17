package es.adevinta.spain.friends.integrationTests

import com.github.tomakehurst.wiremock.WireMockServer
import com.github.tomakehurst.wiremock.common.ConsoleNotifier
import com.github.tomakehurst.wiremock.core.WireMockConfiguration.options
import es.adevinta.spain.friends.FriendsApplication
import es.adevinta.spain.friends.domain.contracts.FriendshipRepository
import es.adevinta.spain.friends.domain.contracts.UserRepository
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
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.setup.DefaultMockMvcBuilder
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.springframework.web.context.WebApplicationContext

@SpringBootTest(
  classes = [FriendsApplication::class]
)
@ExtendWith(SpringExtension::class)
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
  }

}


