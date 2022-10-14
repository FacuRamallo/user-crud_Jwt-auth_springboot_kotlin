package es.adevinta.spain.friends.application

import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.argumentCaptor
import com.nhaarman.mockito_kotlin.given
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.never
import com.nhaarman.mockito_kotlin.verify
import es.adevinta.spain.friends.application.auth.RegisterUser
import es.adevinta.spain.friends.application.auth.commands.NewUserCommand
import es.adevinta.spain.friends.domain.Role.ROLE_USER
import es.adevinta.spain.friends.domain.contracts.UserRepository
import es.adevinta.spain.friends.domain.exceptions.NameAlreadyExistException
import es.adevinta.spain.friends.domain.User
import es.adevinta.spain.friends.domain.UserName
import es.adevinta.spain.friends.domain.contracts.PasswordEncoderService
import es.adevinta.spain.friends.domain.exceptions.InvalidPasswordException
import es.adevinta.spain.friends.domain.exceptions.InvalidUsernameException
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

private val newUserWhithWrongUserName = NewUserCommand("usuario1+`´ç", "abcd1234", setOf("ROLE_USER"))
private val newUserWhithWrongPassword = NewUserCommand("usuario1", "abc1234`+ç´¡''¡¡", setOf("ROLE_USER"))
private val newUserCommand = NewUserCommand("usuarioOK", "abcd12345", setOf("ROLE_USER"))
private val newUser = User(UserName("usuarioOK"),"abce12345", setOf(ROLE_USER))
class RegisterUserShould {

  private val userRepository = mock<UserRepository>()
  private val passwordEncoderService = mock<PasswordEncoderService>()
  private val registerUser = RegisterUser(userRepository,passwordEncoderService)

  @Test
  fun `fail when userName contains non alphanumerical characters`(){
    val expectedMessage = InvalidUsernameException().message

    val exception = assertFailsWith<InvalidUsernameException> {
      registerUser.execute(newUserWhithWrongUserName)
    }

    assertEquals(expectedMessage, exception.message)
    verify(userRepository, never()).add(newUser)
  }

  @Test
  fun `fail when password contains non alphanumerical characters`(){
    val expectedMessage = InvalidPasswordException().message

    val exception = assertFailsWith<InvalidPasswordException> {
      registerUser.execute(newUserWhithWrongPassword)
    }

    assertEquals(expectedMessage, exception.message)
    verify(userRepository, never()).add(newUser)
  }

  @Test
  fun `fail when userName already exist`(){
    val expectedMessage = NameAlreadyExistException(newUser.username.value).message

    given {userRepository.exist(newUser.username)} .willReturn(true)
    given {passwordEncoderService.encodePassword(any())} .willReturn("encodedPassword")

    val exception = assertFailsWith<NameAlreadyExistException> {
      registerUser.execute(newUserCommand)
    }

    assertEquals(expectedMessage, exception.message)
    verify(userRepository, never()).add(newUser)

  }

  @Test
  fun `add new user when it does not exist`(){

    given {userRepository.exist(newUser.username)} .willReturn(false)

    given {passwordEncoderService.encodePassword(any())} .willReturn("encodedPasword")

    registerUser.execute(newUserCommand)

    val  userCaptorAdd = argumentCaptor<User>()

    verify(userRepository).add(userCaptorAdd.capture())

    assertEquals(newUser.username,userCaptorAdd.firstValue.username)
    assertEquals("encodedPasword",userCaptorAdd.firstValue.password)

  }

  @Test
  fun `create new user with default ROLE= ROLE_USER`(){

    given {userRepository.exist(newUser.username)} .willReturn(false)

    given {passwordEncoderService.encodePassword(any())} .willReturn("encodedPassword")

    registerUser.execute(newUserCommand)

    val  userCaptorAdd = argumentCaptor<User>()

    verify(userRepository).add(userCaptorAdd.capture())

    assertEquals(newUser.roles?.let {  it.elementAt(0).name }, userCaptorAdd.firstValue.roles?.let{it.elementAt(0).name})

  }
}
