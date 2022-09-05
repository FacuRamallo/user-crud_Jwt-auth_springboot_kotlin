package es.adevinta.spain.friends.application.auth

import es.adevinta.spain.friends.domain.PassWord
import es.adevinta.spain.friends.domain.User
import es.adevinta.spain.friends.domain.UserName
import es.adevinta.spain.friends.domain.contracts.IUserAuthenticationService

class AuthenticateUser(
  private val userAuthenticationService: IUserAuthenticationService,
) {

  fun authenticate(userCommand: UserCommand): AuthUserCommand {

    val userToAuthenticate = User(UserName(userCommand.userName), PassWord(userCommand.passWord).value)

    userAuthenticationService.authenticateUser(userToAuthenticate.username.value, userToAuthenticate.password)

    return userAuthenticationService.getAuthenticatedUserDetails()
  }

}
