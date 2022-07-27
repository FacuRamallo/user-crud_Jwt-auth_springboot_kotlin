package es.adevinta.spain.friends.application

import es.adevinta.spain.friends.domain.contracts.UserRepository
import es.adevinta.spain.friends.infrastructure.controller.dtos.NewUserDto

class GetUsers(private val userRepository : UserRepository) {

  fun getAllUsers(): List<NewUserDto>{
    val users = userRepository.getAll()
    return users.map { NewUserDto(it.username.value,it.password.value) }
  }
}
