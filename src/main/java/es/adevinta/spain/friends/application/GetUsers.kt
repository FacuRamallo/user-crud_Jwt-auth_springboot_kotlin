package es.adevinta.spain.friends.application

import es.adevinta.spain.friends.domain.contracts.UserRepository
import es.adevinta.spain.friends.infrastructure.controller.dtos.SignInDto

class GetUsers(private val userRepository : UserRepository) {

  fun getAllUsers(): List<SignInDto>{
    val users = userRepository.getAll()
    return users.map { SignInDto(it.username.value,it.password, emptySet()) }
  }
}
