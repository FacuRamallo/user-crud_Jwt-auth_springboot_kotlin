package es.adevinta.spain.friends.infrastructure.apiResponses

import es.adevinta.spain.friends.infrastructure.controller.dtos.SignInDto
import es.adevinta.spain.friends.domain.Friend
import es.adevinta.spain.friends.infrastructure.controller.dtos.LoggedUserDto
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.HttpStatus.BAD_REQUEST
import org.springframework.http.HttpStatus.CREATED
import org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR
import org.springframework.http.HttpStatus.OK
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity

enum class ApiResponses(
  private val message: String,
  private val code: String,
  private val statusCode: HttpStatus,
){
  OK_201(
    "New User created successfully",
    "Ok_201",
    CREATED
  ),
  OK_202(
    "User authenticated",
    "Ok_202",
    OK
  ),
  ERROR_100(
    "User name must contain from 5 to 10 alphanumerical characters",
    "Error_100",
    BAD_REQUEST
  ),
  ERROR_101(
    "Password must contain from 8 to 12 alphanumerical characters",
    "Error_101",
    BAD_REQUEST
  ),
  ERROR_102(
    "Username already taken",
    "Error_102",
    BAD_REQUEST
  ),
  ERROR_103(
    "An error occured while creating the new user, Please try again later.",
    "Error_102",
    INTERNAL_SERVER_ERROR
  );

  fun response(): ResponseEntity<String> {
    val responseHeaders = HttpHeaders()
    responseHeaders.contentType = MediaType.APPLICATION_JSON

    return ResponseEntity(
      "{\"Message\":\"$message\",\"StatusCode\":${statusCode.value()},\"Code\":\"$code\"}",
      responseHeaders,
      statusCode
    )
  }

  fun jwtResponse(loggedUser: LoggedUserDto): ResponseEntity<String> {
    val responseHeaders = HttpHeaders()
    responseHeaders.contentType = MediaType.APPLICATION_JSON

    val responseBody = "{\"Message\":\"$message\",\"StatusCode\":${statusCode.value()},\"Code\":\"$code\",\"Username\":\"${loggedUser.username}\",\"roles\":\"${loggedUser.roles}\",\"token\":\"${loggedUser.token}\",\"tokenType\":\"${loggedUser.tokenType}\"}"

    return ResponseEntity(
      responseBody,
      responseHeaders,
      statusCode
    )
  }
  fun friendsResponse(friends: List<Friend?>): ResponseEntity<String> {
    val responseHeaders = HttpHeaders()
    responseHeaders.contentType = MediaType.APPLICATION_JSON

    val responseBody = friends.map { "{\"Username\":\"${it?.friendName}\",\"IsAccepted\":${it?.accepted}"}

    return ResponseEntity(
      "$responseBody",
      responseHeaders,
      statusCode
    )
  }

}
