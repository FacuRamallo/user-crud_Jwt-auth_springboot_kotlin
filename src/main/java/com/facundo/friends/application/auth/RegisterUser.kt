package com.facundo.friends.application.auth

import com.facundo.friends.application.auth.commands.NewUserCommand
import com.facundo.friends.domain.PassWord
import com.facundo.friends.domain.Role
import com.facundo.friends.domain.Role.ROLE_ADMIN
import com.facundo.friends.domain.Role.ROLE_USER
import com.facundo.friends.domain.User
import com.facundo.friends.domain.UserName
import com.facundo.friends.domain.contracts.PasswordEncoderService
import com.facundo.friends.domain.contracts.UserRepository
import com.facundo.friends.domain.exceptions.NameAlreadyExistException


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
