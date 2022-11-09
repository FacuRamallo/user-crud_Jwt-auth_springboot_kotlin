package com.facundo.friends.application.auth

import com.facundo.friends.application.auth.commands.AuthenticateUserCommand
import com.facundo.friends.application.auth.dtos.AuthUserDto
import com.facundo.friends.domain.PassWord
import com.facundo.friends.domain.User
import com.facundo.friends.domain.UserName
import com.facundo.friends.domain.contracts.UserAuthenticationService

class AuthenticateUser(
  private val userAuthenticationService: UserAuthenticationService,
) {

  fun execute(authenticateUserCommand: AuthenticateUserCommand): AuthUserDto {

    val userToAuthenticate =
      User(
        UserName(authenticateUserCommand.userName),
        PassWord(authenticateUserCommand.passWord).value,
        emptySet()
      )

    userAuthenticationService.authenticateUser(userToAuthenticate.username.value, userToAuthenticate.password)

    return userAuthenticationService.getAuthenticatedUserDetails()
  }

  fun getCurrentUser(): String {
    return userAuthenticationService.getAuthenticatedUserName()
  }
}
