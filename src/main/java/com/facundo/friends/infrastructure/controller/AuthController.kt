package com.facundo.friends.infrastructure.controller

import com.facundo.friends.application.auth.AuthenticateUser
import com.facundo.friends.application.auth.RegisterUser
import com.facundo.friends.application.auth.commands.AuthenticateUserCommand
import com.facundo.friends.application.auth.commands.NewUserCommand
import com.facundo.friends.domain.exceptions.InvalidPasswordException
import com.facundo.friends.domain.exceptions.InvalidUsernameException
import com.facundo.friends.domain.exceptions.NameAlreadyExistException
import com.facundo.friends.domain.exceptions.UserDetailsException
import com.facundo.friends.infrastructure.apiResponses.ApiResponses.ERROR_100
import com.facundo.friends.infrastructure.apiResponses.ApiResponses.ERROR_101
import com.facundo.friends.infrastructure.apiResponses.ApiResponses.ERROR_102
import com.facundo.friends.infrastructure.apiResponses.ApiResponses.ERROR_103
import com.facundo.friends.infrastructure.apiResponses.ApiResponses.OK_201
import com.facundo.friends.infrastructure.apiResponses.ApiResponses.OK_202
import com.facundo.friends.infrastructure.controller.dtos.SignInDto
import com.facundo.friends.infrastructure.controller.dtos.SignUpDto
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController


@RestController
class AuthController(val registerUser: RegisterUser, val authenticateUser: AuthenticateUser) {

  @PostMapping("v1/authenticate")
  fun signIn(@RequestBody user: SignInDto): ResponseEntity<String>{

    val command = AuthenticateUserCommand(user.userName, user.password)

    val authUserDetails = authenticateUser.execute(command)

    return OK_202.jwtResponse(authUserDetails)
  }

  @PostMapping("v1/signup")
  fun signUp(@RequestBody user: SignUpDto): ResponseEntity<String> {
    val userToRegister = NewUserCommand(user.userName,user.password, user.roles)

    registerUser.execute(userToRegister)

    return OK_201.response()
  }

  @PostMapping("v1/authenticatedUser")
  fun authenticatedUserName(): ResponseEntity<String> {

    val currentUser = authenticateUser.getCurrentUser()

    return OK_201.currentUserResponse(currentUser)
  }

  @ExceptionHandler(value = [UserDetailsException::class])
  fun handleUserDetailsException(e: UserDetailsException): ResponseEntity<String> =
    when (e) {
      is InvalidUsernameException -> ERROR_100.response()
      is InvalidPasswordException -> ERROR_101.response()
      is NameAlreadyExistException -> ERROR_102.response()
      else -> ERROR_103.response()
    }


}
