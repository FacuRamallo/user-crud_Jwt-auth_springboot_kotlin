package es.adevinta.spain.friends.application.auth

import es.adevinta.spain.friends.domain.PassWord
import es.adevinta.spain.friends.domain.User
import es.adevinta.spain.friends.domain.UserName
import es.adevinta.spain.friends.domain.contracts.PasswordEncoderService
import es.adevinta.spain.friends.domain.contracts.UserRepository
import es.adevinta.spain.friends.domain.exceptions.NameAlreadyExistException


open class RegisterUser(
  private val userRepository : UserRepository,
  private val passwordEncoder : PasswordEncoderService,
) {

  open fun create(userCommand: UserCommand) {
    val newUser = createUserFromCommand(userCommand)
    val userNameAlreadyExist = userRepository.exist(newUser.username);
    if(userNameAlreadyExist) throw NameAlreadyExistException(newUser.username.value)
    userRepository.add(newUser)
  }

  private fun createUserFromCommand(userCommand: UserCommand) : User {
    val userPassWord : PassWord = PassWord(userCommand.passWord)
    val userPasswordEncoded= passwordEncoder.encodePassword(userPassWord)
    return User(UserName(userCommand.userName), userPasswordEncoded)
  }

}
