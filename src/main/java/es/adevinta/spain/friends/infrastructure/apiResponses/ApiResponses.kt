package es.adevinta.spain.friends.infrastructure.apiResponses

import es.adevinta.spain.friends.infrastructure.controller.dtos.NewUserDto
import es.adevinta.spain.friends.domain.Friend
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.HttpStatus.CREATED
import org.springframework.http.HttpStatus.OK
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity

enum class ApiResponses(
  private val message: String,
  private val code: String,
  private val statusCode: HttpStatus,
){
  OK_201(
    "Nuevo usuario creado con éxito",
    "Ok_201",
    CREATED
  ),
  OK_202(
  "Registered Users",
  "Ok_202",
    OK
  ),
  OK_203(
    "Frienship request sent",
    "Ok_203",
    OK
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

  fun usersResponse(users: List<NewUserDto>): ResponseEntity<String> {
    val responseHeaders = HttpHeaders()
    responseHeaders.contentType = MediaType.APPLICATION_JSON

    val responseBody = users.map { "{\"Username\":\"${it.userName}\",\"Password\":${it.password}}"}

    return ResponseEntity(
      "$responseBody",
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
