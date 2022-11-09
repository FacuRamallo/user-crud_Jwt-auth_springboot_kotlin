package com.facundo.friends.application.auth

import es.adevinta.spain.friends.application.auth.commands.NewUserCommand
import es.adevinta.spain.friends.domain.PassWord
import es.adevinta.spain.friends.domain.Role
import es.adevinta.spain.friends.domain.Role.ROLE_ADMIN
import es.adevinta.spain.friends.domain.Role.ROLE_USER
import es.adevinta.spain.friends.domain.User
import es.adevinta.spain.friends.domain.UserName
import es.adevinta.spain.friends.domain.contracts.PasswordEncoderService
import es.adevinta.spain.friends.domain.contracts.UserRepository
import es.adevinta.spain.friends.domain.exceptions.NameAlreadyExistException


open class RegisterUser(
  private val userRepository : UserRepository,
  private val passwordEncoder : PasswordEncoderService,
) {

  open fun execute(newUserCommand: NewUserCommand) {
    val newUser = createUserFromCommand(newUserCommand)
    val userNameAlreadyExist = userRepository.exist(newUser.username);
    if(userNameAlreadyExist) throw NameAlreadyExistException(newUser.username.value)
    userRepository.add(newUser)
  }

  private fun createUserFromCommand(newUserCommand: NewUserCommand) : User {
    val userPassWord : PassWord = PassWord(newUserCommand.passWord)
    val userPasswordEncoded= passwordEncoder.encodePassword(userPassWord.value)
    val role = mutableSetOf<Role>()
    newUserCommand.roles.apply { if (isNullOrEmpty()) role.add(ROLE_USER) } ?.forEach {
      when (it) {
        "ROLE_USER" -> role.add(ROLE_USER)
        "ROLE_ADMIN" -> role.add(ROLE_ADMIN)
      }
    }
    return User(UserName(newUserCommand.userName), userPasswordEncoded, role)
  }

}
