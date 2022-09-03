package es.adevinta.spain.friends.application.auth

import es.adevinta.spain.friends.domain.PassWord
import es.adevinta.spain.friends.domain.User
import es.adevinta.spain.friends.domain.UserName
import es.adevinta.spain.friends.domain.contracts.UserRepository
import es.adevinta.spain.friends.domain.exceptions.NameAlreadyExistException
import es.adevinta.spain.friends.infrastructure.controller.dtos.SignInDto
import es.adevinta.spain.friends.services.PasswordEncoderService



open class RegisterUser(
  private val userRepository : UserRepository,
  private val passwordEncoder : PasswordEncoderService,
) {

  open fun create(signInDto: SignInDto) {
    val newUser = createUserFromDto(signInDto)
    val userNameAlreadyExist = userRepository.exist(newUser.username);
    if(userNameAlreadyExist) throw NameAlreadyExistException(newUser.username.value)
    userRepository.add(newUser)
  }

  private fun createUserFromDto(signInDto: SignInDto) : User {
    val userPassWord : PassWord = PassWord(signInDto.password)
    val userPasswordEncoded= passwordEncoder.encodePassword(userPassWord)
    return User(UserName(signInDto.userName), userPasswordEncoded)
  }

}
