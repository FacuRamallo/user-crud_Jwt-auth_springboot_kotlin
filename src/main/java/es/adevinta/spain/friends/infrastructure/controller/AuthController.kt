package es.adevinta.spain.friends.infrastructure.controller

import es.adevinta.spain.friends.application.auth.AuthenticateUser
import es.adevinta.spain.friends.application.auth.RegisterUser
import es.adevinta.spain.friends.application.auth.AuthenticateUserCommand
import es.adevinta.spain.friends.application.auth.NewUserCommand
import es.adevinta.spain.friends.domain.exceptions.InvalidPasswordException
import es.adevinta.spain.friends.domain.exceptions.InvalidUsernameException
import es.adevinta.spain.friends.domain.exceptions.NameAlreadyExistException
import es.adevinta.spain.friends.domain.exceptions.UserDetailsException
import es.adevinta.spain.friends.infrastructure.apiResponses.ApiResponses.ERROR_100
import es.adevinta.spain.friends.infrastructure.apiResponses.ApiResponses.ERROR_101
import es.adevinta.spain.friends.infrastructure.apiResponses.ApiResponses.ERROR_102
import es.adevinta.spain.friends.infrastructure.apiResponses.ApiResponses.ERROR_103
import es.adevinta.spain.friends.infrastructure.apiResponses.ApiResponses.OK_201
import es.adevinta.spain.friends.infrastructure.apiResponses.ApiResponses.OK_202
import es.adevinta.spain.friends.infrastructure.controller.dtos.SignInDto
import es.adevinta.spain.friends.infrastructure.controller.dtos.SignUpDto
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
    val userToRegister = NewUserCommand(user.userName,user.password, user.roles?: setOf("ROLE_USER"))

    registerUser.create(userToRegister)

    return OK_201.response()
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
