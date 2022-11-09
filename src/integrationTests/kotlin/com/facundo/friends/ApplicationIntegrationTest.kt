package com.facundo.friends

import com.facundo.friends.ApplicationIntegrationTest.Companion.dockerComposeContainer
import com.facundo.friends.integrationTests.acceptance.AuthenticateUserFeature
import com.facundo.friends.integrationTests.acceptance.FriendshipHandlerFeature
import com.facundo.friends.integrationTests.acceptance.ListFriendsFeature
import com.facundo.friends.integrationTests.acceptance.RegisterUserFeature
import com.facundo.friends.integrationTests.database.DatabaseTestCase
import com.facundo.friends.integrationTests.helper.DockerComposeHelper
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
  @Nested
  inner class AuthenticateUserFeatureNested : AuthenticateUserFeature()
  @Nested
  inner class FriendshipFeatureHandlerNested : FriendshipHandlerFeature()
  @Nested
  inner class ListFriendsFeatureNested : ListFriendsFeature()

}
