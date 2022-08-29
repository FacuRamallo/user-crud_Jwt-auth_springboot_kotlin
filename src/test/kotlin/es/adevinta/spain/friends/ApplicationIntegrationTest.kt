package es.adevinta.spain.friends

import es.adevinta.spain.friends.infraestructure.acceptance.RegisterUserFeature
import es.adevinta.spain.friends.infraestructure.database.DatabaseTestCase
import es.adevinta.spain.friends.infraestructure.helper.DockerComposeHelper
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Nested
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers

@Testcontainers
class ApplicationIntegrationTest {
  companion object {

    @Container
    private val dockerComposeContainer = DockerComposeHelper.create()

    @BeforeAll
    @JvmStatic
    fun setSystemProperties() {
      DockerComposeHelper.setSystemProperties(dockerComposeContainer)
    }
  }

  @Nested
  inner class DatabaseTestCaseNested : DatabaseTestCase()

  @Nested
  inner class RegisterUserFeatureNested : RegisterUserFeature()

}
