package es.adevinta.spain.friends.domain.contracts

import es.adevinta.spain.friends.application.auth.dtos.AuthUserDto


interface UserAuthenticationService {

  fun authenticateUser(username: String, password: String)

  fun getAuthenticatedUserToken() : String?

  fun getAuthenticatedUserDetails() : AuthUserDto

  fun getAuthenticatedUserName(): String

}
