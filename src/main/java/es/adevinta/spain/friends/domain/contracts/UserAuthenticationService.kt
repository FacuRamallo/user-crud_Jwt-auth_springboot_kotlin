package es.adevinta.spain.friends.domain.contracts

import es.adevinta.spain.friends.application.auth.AuthUserDto


interface UserAuthenticationService {

  fun authenticateUser(username: String, password: String)

  fun getAuthenticatedUserToken() : String?

  fun getAuthenticatedUserDetails() : AuthUserDto
}
