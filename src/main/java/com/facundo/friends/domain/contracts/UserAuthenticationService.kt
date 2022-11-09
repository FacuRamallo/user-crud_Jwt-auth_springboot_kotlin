package com.facundo.friends.domain.contracts

import com.facundo.friends.application.auth.dtos.AuthUserDto


interface UserAuthenticationService {

  fun authenticateUser(username: String, password: String)

  fun getAuthenticatedUserToken() : String?

  fun getAuthenticatedUserDetails() : AuthUserDto

  fun getAuthenticatedUserName(): String

}
