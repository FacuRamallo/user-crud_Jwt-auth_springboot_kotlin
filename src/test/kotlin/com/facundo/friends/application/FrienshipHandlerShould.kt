package com.facundo.friends.application

import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.argumentCaptor
import com.nhaarman.mockito_kotlin.given
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.times
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.willReturn
import com.facundo.friends.application.commands.FriendshipUpdateCommand
import com.facundo.friends.application.commands.NewFriendshipRequestCommand
import com.facundo.friends.domain.FriendshipStatus
import com.facundo.friends.domain.FriendshipStatus.ACCEPTED
import com.facundo.friends.domain.FriendshipStatus.CANCELED
import com.facundo.friends.domain.Role.ROLE_USER
import com.facundo.friends.domain.User
import com.facundo.friends.domain.UserName
import com.facundo.friends.domain.contracts.FriendshipRepository
import com.facundo.friends.domain.contracts.UserAuthenticationService
import com.facundo.friends.domain.contracts.UserRepository
import com.facundo.friends.domain.exceptions.FriendshipAlreadyExistException
import com.facundo.friends.domain.exceptions.SelfFriendshipException
import com.facundo.friends.domain.exceptions.UserNameNotFoundException
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

    friendshipHandler.execute(NewFriendshipRequestCommand(target.username.value))

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
  fun `allow a registered user to accept requested friendship`(){
    given{userRepository.exist(target.username)}.willReturn(true)
    given{userAuthenticationService.getAuthenticatedUserName()}.willReturn("currUser")
    given{friendshipRepository.existBetween(any(), any())}.willReturn(true)

    friendshipHandler.execute(FriendshipUpdateCommand(target.username.value, "ACCEPTED"))

    val friendshipRequesterArgCaptor = argumentCaptor<UserName>()
    val friendshipTargetArgCaptor = argumentCaptor<UserName>()
    val friendshipStatusArgCaptor = argumentCaptor<FriendshipStatus>()

    verify(friendshipRepository).updateStatus(
      friendshipRequesterArgCaptor.capture(),
      friendshipTargetArgCaptor.capture(),
      friendshipStatusArgCaptor.capture()
    )

    assertEquals(requester.username,friendshipRequesterArgCaptor.firstValue)
    assertEquals(target.username,friendshipTargetArgCaptor.firstValue)
    assertEquals(ACCEPTED,friendshipStatusArgCaptor.firstValue)

  }

  @Test
  fun `allow a registered user to decline requested friendship`(){
    given{userRepository.exist(target.username)}.willReturn(true)
    given{userAuthenticationService.getAuthenticatedUserName()}.willReturn("currUser")
    given{friendshipRepository.existBetween(any(), any())}.willReturn(true)

    friendshipHandler.execute(FriendshipUpdateCommand(target.username.value, "CANCELED"))

    val friendshipRequesterArgCaptor = argumentCaptor<UserName>()
    val friendshipTargetArgCaptor = argumentCaptor<UserName>()
    val friendshipStatusArgCaptor = argumentCaptor<FriendshipStatus>()

    verify(friendshipRepository).updateStatus(
      friendshipRequesterArgCaptor.capture(),
      friendshipTargetArgCaptor.capture(),
      friendshipStatusArgCaptor.capture()
    )

    assertEquals(requester.username,friendshipRequesterArgCaptor.firstValue)
    assertEquals(target.username,friendshipTargetArgCaptor.firstValue)
    assertEquals(CANCELED,friendshipStatusArgCaptor.firstValue)

  }

  @Test
  fun `fail if target user does't exist`(){
    given{userRepository.exist(target.username)}.willReturn(false)
    given{userAuthenticationService.getAuthenticatedUserName()}.willReturn("currUser")

    assertFailsWith<UserNameNotFoundException> {
      friendshipHandler.execute(NewFriendshipRequestCommand(target.username.value))
    }
  }

  @Test
  fun `fail if a user request friendship to himself`(){
    given{userRepository.exist(requester.username)}.willReturn(true)
    given{userAuthenticationService.getAuthenticatedUserName()}.willReturn("currUser")

    assertFailsWith<SelfFriendshipException> {
      friendshipHandler.execute(NewFriendshipRequestCommand(requester.username.value))
    }
  }

  @Test
  fun `fail if user request friendship to a user that already has a pending request from him`(){
    given{userRepository.exist(requester.username)}.willReturn(true)
    given{userRepository.exist(target.username)}.willReturn(true)
    given{userAuthenticationService.getAuthenticatedUserName()}.willReturn("currUser")
    given{friendshipRepository.existBetween(requester.username, target.username)}.willReturn { true }

    assertFailsWith<FriendshipAlreadyExistException> {
      friendshipHandler.execute(NewFriendshipRequestCommand(target.username.value))
    }
  }
}
