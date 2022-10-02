package es.adevinta.spain.friends.application

import com.nhaarman.mockito_kotlin.argumentCaptor
import com.nhaarman.mockito_kotlin.given
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.times
import com.nhaarman.mockito_kotlin.verify
import es.adevinta.spain.friends.domain.Role.ROLE_USER
import es.adevinta.spain.friends.domain.User
import es.adevinta.spain.friends.domain.UserName
import es.adevinta.spain.friends.domain.contracts.FriendshipRepository
import es.adevinta.spain.friends.domain.contracts.UserAuthenticationService
import es.adevinta.spain.friends.domain.contracts.UserRepository
import es.adevinta.spain.friends.domain.exceptions.SelfFriendshipException
import es.adevinta.spain.friends.domain.exceptions.TargetUserNameNotFoundException
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import org.junit.jupiter.api.Test

class FrienshipHandlerShould {

  private val userRepository = mock<UserRepository>()
  private val friendshipRepository = mock<FriendshipRepository>()
  private val userAuthenticationService = mock<UserAuthenticationService>()
  private val friendshipHandler = FrienshipHandler(userRepository,friendshipRepository,userAuthenticationService)

  private val requester: User = User(UserName("currUser"),"password1", setOf(ROLE_USER))
  private val target: User = User(UserName("targetUser"),"password2", setOf(ROLE_USER))
  @Test
  fun `allow a registered user to requests friendship to another registered user`(){
    given{userRepository.exist(target.username)}.willReturn(true)
    given{userAuthenticationService.getAuthenticatedUserName()}.willReturn("currUser")

    friendshipHandler.execute(FriendshipRequestCommand(target.username.value))

    val friendshipRequesterArgCaptor = argumentCaptor<UserName>()
    val friendshipTargetArgCaptor = argumentCaptor<UserName>()

    verify(friendshipRepository, times(1)).newFriendship(
      friendshipRequesterArgCaptor.capture(),
      friendshipTargetArgCaptor.capture()
    )

    assertEquals(requester.username,friendshipRequesterArgCaptor.firstValue)
    assertEquals(target.username,friendshipTargetArgCaptor.firstValue)
  }

  @Test
  fun `fail if target user does't exist`(){
    given{userRepository.exist(target.username)}.willReturn(false)
    given{userAuthenticationService.getAuthenticatedUserName()}.willReturn("currUser")

    assertFailsWith<TargetUserNameNotFoundException> {
      friendshipHandler.execute(FriendshipRequestCommand(target.username.value))
    }
  }

  @Test
  fun `fail if a user request friendship to himself`(){
    given{userRepository.exist(requester.username)}.willReturn(true)
    given{userAuthenticationService.getAuthenticatedUserName()}.willReturn("currUser")

    assertFailsWith<SelfFriendshipException> {
      friendshipHandler.execute(FriendshipRequestCommand(requester.username.value))
    }
  }
}
