package es.adevinta.spain.friends.application.auth

import es.adevinta.spain.friends.domain.PassWord
import es.adevinta.spain.friends.domain.Role
import es.adevinta.spain.friends.domain.User
import es.adevinta.spain.friends.domain.UserName
import es.adevinta.spain.friends.domain.contracts.UserAuthenticationService

class AuthenticateUser(
  private val userAuthenticationService: UserAuthenticationService,
) {

  fun execute(userCommand: UserCommand): AuthUserDto {

    val userToAuthenticate = User(UserName(userCommand.userName), PassWord(userCommand.passWord).value, mapToRoleSet(userCommand.roles))

    userAuthenticationService.authenticateUser(userToAuthenticate.username.value, userToAuthenticate.password)

    return userAuthenticationService.getAuthenticatedUserDetails()
  }

  private fun mapToRoleSet(stingRoleSet: Set<String>?):Set<Role>? = stingRoleSet?.map{ Role.valueOf(it) }?.toSet()
}
