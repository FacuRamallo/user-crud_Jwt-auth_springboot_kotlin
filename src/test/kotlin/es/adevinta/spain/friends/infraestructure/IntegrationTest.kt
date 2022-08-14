package es.adevinta.spain.friends.infraestructure

import com.github.tomakehurst.wiremock.WireMockServer
import com.github.tomakehurst.wiremock.common.ConsoleNotifier
import com.github.tomakehurst.wiremock.core.WireMockConfiguration.options
import es.adevinta.spain.friends.FriendsApplication
import es.adevinta.spain.friends.domain.contracts.FriendshipRepository
import es.adevinta.spain.friends.domain.contracts.UserRepository
import io.restassured.module.mockmvc.RestAssuredMockMvc.mockMvc
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.BeforeEach
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.core.env.Environment
import org.springframework.security.test.context.support.WithSecurityContext
import org.springframework.test.web.servlet.MockMvc

@SpringBootTest(
  classes = [FriendsApplication::class],
  properties = ["spring.profiles.active=integration-test"]
)
@AutoConfigureMockMvc
class IntegrationTest {
  @Autowired
  lateinit var mvc: MockMvc

  @Autowired
  lateinit var env: Environment

  @Autowired
  lateinit var userRepository: UserRepository

  @Autowired
  lateinit var friendshipRepository: FriendshipRepository

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
    mockMvc(mvc)
    wireMockServer.resetAll()
  }
}


