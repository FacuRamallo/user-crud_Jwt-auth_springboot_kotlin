package es.adevinta.spain.friends.application.auth

import es.adevinta.spain.friends.application.auth.commands.AuthenticateUserCommand
import es.adevinta.spain.friends.application.auth.dtos.AuthUserDto
import es.adevinta.spain.friends.domain.PassWord
import es.adevinta.spain.friends.domain.Role
import es.adevinta.spain.friends.domain.User
import es.adevinta.spain.friends.domain.UserName
import es.adevinta.spain.friends.domain.contracts.UserAuthenticationService

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
