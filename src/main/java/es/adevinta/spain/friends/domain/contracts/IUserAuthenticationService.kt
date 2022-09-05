package es.adevinta.spain.friends.domain.contracts

import es.adevinta.spain.friends.application.auth.AuthUserCommand


interface IUserAuthenticationService {

  fun authenticateUser(username: String, password: String)

  fun getAuthenticatedUserToken() : String?

  fun getAuthenticatedUserDetails() : AuthUserCommand
}
