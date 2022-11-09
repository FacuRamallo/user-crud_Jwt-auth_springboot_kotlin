package com.facundo.friends.application

import com.nhaarman.mockito_kotlin.given
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.willReturn
import es.adevinta.spain.friends.application.dtos.FriendDto
import es.adevinta.spain.friends.domain.Friend
import es.adevinta.spain.friends.domain.FriendshipStatus.ACCEPTED
import es.adevinta.spain.friends.domain.Role.ROLE_USER
import es.adevinta.spain.friends.domain.User
import es.adevinta.spain.friends.domain.UserName
import es.adevinta.spain.friends.domain.contracts.FriendshipRepository
import es.adevinta.spain.friends.domain.contracts.UserAuthenticationService
import kotlin.test.assertEquals
import org.junit.jupiter.api.Test

class GetFriensListShould {
  private val friendshipRepository = mock<FriendshipRepository>()
  private val userAuthenticationService =  mock<UserAuthenticationService>()
  private val getFriendsList = GetFriendsList(friendshipRepository,userAuthenticationService)


  @Test
  fun `return null when current authenticated user have no friends`() {
    val currUser = User(UserName("user001"), "123654789", setOf(ROLE_USER))

    val expectedFriendsList = emptyList<FriendDto>()

    given { userAuthenticationService.getAuthenticatedUserName() }.willReturn { currUser.username.value }
    given { friendshipRepository.getFriends(currUser.username) }.willReturn { emptyList() }

    val sut = getFriendsList.execute()

    assertEquals(expectedFriendsList, sut)
  }

  @Test
  fun `return a list of accepted friends for current authenticated user`(){
    val currUser = User(UserName("user001"), "123654789",setOf(ROLE_USER))
    val user003 = User(UserName("user003"),"qwert12345", setOf(ROLE_USER))
    val user004 = User(UserName("user004"),"qwert12345", setOf(ROLE_USER))
    val user005 = User(UserName("user005"),"qwert12345", setOf(ROLE_USER))

    val friendsListFromDB = listOf(
      Friend(user003.username, ACCEPTED),
      Friend(user004.username, ACCEPTED),
      Friend(user005.username, ACCEPTED)
    )

    val expectedFriendsList = listOf(
      FriendDto(user003.username.value),
      FriendDto(user004.username.value),
      FriendDto(user005.username.value)
    )

    given { userAuthenticationService.getAuthenticatedUserName() }.willReturn { currUser.username.value }
    given { friendshipRepository.getFriends(currUser.username) }.willReturn { friendsListFromDB }

    val sut = getFriendsList.execute()

    assertEquals(expectedFriendsList,sut)

  }
}
