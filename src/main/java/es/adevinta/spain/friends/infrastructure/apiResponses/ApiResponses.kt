package es.adevinta.spain.friends.infrastructure.apiResponses

import es.adevinta.spain.friends.application.auth.AuthUserDto
import es.adevinta.spain.friends.domain.Friend
import java.lang.Exception
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
  OK_203(
    "Friendship request sent successfully",
    "Ok_203",
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
  ),
  ERROR_104(
    "Friendship request error.",
    "Error_104",
    BAD_REQUEST
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

  fun jwtResponse(authenticatedUser: AuthUserDto): ResponseEntity<String> {
    val responseHeaders = HttpHeaders()
    responseHeaders.contentType = MediaType.APPLICATION_JSON

    val responseBody =
      "{" +
        "\"Message\":\"$message\"," +
        "\"StatusCode\":${statusCode.value()}," +
        "\"Code\":\"$code\"," +
        "\"Username\":\"${authenticatedUser.username}\"," +
        "\"roles\":\"${authenticatedUser.roles}\"," +
        "\"token\":\"${authenticatedUser.token}\"," +
        "\"tokenType\":\"${authenticatedUser.tokenType}\"" +
      "}"

    return ResponseEntity(
      responseBody,
      responseHeaders,
      statusCode
    )
  }

  fun currentUserResponse(currentUserName: String): ResponseEntity<String> {
    val responseHeaders = HttpHeaders()
    responseHeaders.contentType = MediaType.APPLICATION_JSON

    val responseBody =  "{\"CurrentUserName\":\"${currentUserName}\"}"

    return ResponseEntity(
      responseBody,
      responseHeaders,
      statusCode
    )
  }

  fun friendshipErrorResponse(exception: Exception): ResponseEntity<String> {
    val responseHeaders = HttpHeaders()
    responseHeaders.contentType = MediaType.APPLICATION_JSON

    val responseBody =
      "{" +
            "\"Message\":\"$message\"," +
            "\"StatusCode\":${statusCode.value()}," +
            "\"Code\":\"$code\"," +
            "\"Error\":\"${exception.message}\"" +
            "}"

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
