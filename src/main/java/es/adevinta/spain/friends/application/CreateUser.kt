package es.adevinta.spain.friends.application

import es.adevinta.spain.friends.domain.contracts.UserRepository
import es.adevinta.spain.friends.domain.exceptions.NameAlreadyExistException
import es.adevinta.spain.friends.domain.PassWord
import es.adevinta.spain.friends.domain.User
import es.adevinta.spain.friends.domain.UserName
import es.adevinta.spain.friends.infrastructure.controller.dtos.NewUserDto


open class CreateUser(private val userRepository : UserRepository) {

  open fun execute(newUserDto: NewUserDto) {
    val newUser = createUserFromDto(newUserDto)
    val userNameAlreadyExist = userRepository.exist(newUser.username);
    if(userNameAlreadyExist) throw NameAlreadyExistException(newUser.username.value)
    userRepository.add(newUser)
  }

  private fun createUserFromDto(newUserDto: NewUserDto) : User {
    return User(UserName(newUserDto.userName), PassWord(newUserDto.password))
  }

}
