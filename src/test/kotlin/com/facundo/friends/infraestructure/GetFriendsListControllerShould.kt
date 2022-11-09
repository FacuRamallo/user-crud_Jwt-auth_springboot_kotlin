package com.facundo.friends.infraestructure

import com.nhaarman.mockito_kotlin.given
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.whenever
import com.nhaarman.mockito_kotlin.willReturn
import es.adevinta.spain.friends.application.dtos.FriendDto
import es.adevinta.spain.friends.application.GetFriendsList
import es.adevinta.spain.friends.domain.Role.ROLE_USER
import es.adevinta.spain.friends.domain.User
import es.adevinta.spain.friends.domain.UserName
import es.adevinta.spain.friends.infrastructure.apiResponses.ApiResponses.OK_204
import es.adevinta.spain.friends.infrastructure.controller.GetFriendsListController
import kotlin.test.assertEquals
import org.junit.jupiter.api.Test

class GetFriendsListControllerShould {

  private val mockGetFriendsList = mock<GetFriendsList>()
  private val getFriendsListController = GetFriendsListController(mockGetFriendsList)

  @Test
  fun `return null when authenticated user has no friends`(){
    given{ mockGetFriendsList.execute() }.willReturn { emptyList() }
    val expected = OK_204.friendsListResponse(emptyList())

    val sut = getFriendsListController.getFriendsList()

    assertEquals(expected,sut)
  }

  @Test
  fun `return authenticated user list of friends `(){
    val user003 = User(UserName("user003"),"qwert12345", setOf(ROLE_USER))
    val user004 = User(UserName("user004"),"qwert12345", setOf(ROLE_USER))
    val user005 = User(UserName("user005"),"qwert12345", setOf(ROLE_USER))

    val expectedFriendsList = listOf(
      FriendDto(user003.username.value),
      FriendDto(user004.username.value),
      FriendDto(user005.username.value)
    )

    whenever(mockGetFriendsList.execute()).thenReturn( expectedFriendsList )

    val expected = OK_204.friendsListResponse(expectedFriendsList)

    val sut = getFriendsListController.getFriendsList()

    assertEquals(expected,sut)
  }

}
