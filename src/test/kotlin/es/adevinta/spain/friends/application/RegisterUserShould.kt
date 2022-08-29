package es.adevinta.spain.friends.application

import com.nhaarman.mockito_kotlin.argumentCaptor
import com.nhaarman.mockito_kotlin.given
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.never
import com.nhaarman.mockito_kotlin.verify
import es.adevinta.spain.friends.application.auth.RegisterUser
import es.adevinta.spain.friends.domain.contracts.UserRepository
import es.adevinta.spain.friends.domain.exceptions.NameAlreadyExistException
import es.adevinta.spain.friends.domain.PassWord
import es.adevinta.spain.friends.domain.User
import es.adevinta.spain.friends.domain.UserName
import es.adevinta.spain.friends.domain.exceptions.InvalidPasswordException
import es.adevinta.spain.friends.domain.exceptions.InvalidUsernameException
import org.junit.jupiter.api.Test
import es.adevinta.spain.friends.infrastructure.controller.dtos.SignInDto
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

private val newUserWhithWrongUserName = SignInDto("usuario1+`´ç","abc1234")
private val newUserWhithWrongPassword = SignInDto("usuario1","abc1234`+ç´¡''¡¡")
private val signInDto = SignInDto("usuarioOK","abcd1234")
private val newUser = User(UserName("usuarioOK"),PassWord("abcd1234"))
class RegisterUserShould {

  private val userRepository = mock<UserRepository>()
  private val registerUser = RegisterUser(userRepository)

  @Test
  fun `fail when userName contains non alphanumerical characters`(){
    val expectedMessage = InvalidUsernameException().message

    val exception = assertFailsWith<InvalidUsernameException> {
      registerUser.create(newUserWhithWrongUserName)
    }

    assertEquals(expectedMessage, exception.message)
    verify(userRepository, never()).add(newUser)
  }

  @Test
  fun `fail when password contains non alphanumerical characters`(){
    val expectedMessage = InvalidPasswordException().message

    val exception = assertFailsWith<InvalidPasswordException> {
      registerUser.create(newUserWhithWrongPassword)
    }

    assertEquals(expectedMessage, exception.message)
    verify(userRepository, never()).add(newUser)
  }

  @Test
  fun `fail when userName already exist`(){
    val expectedMessage = NameAlreadyExistException(newUser.username.value).message

    given {userRepository.exist(newUser.username)} .willReturn(true)

    val exception = assertFailsWith<NameAlreadyExistException> {
      registerUser.create(signInDto)
    }

    assertEquals(expectedMessage, exception.message)
    verify(userRepository, never()).add(newUser)

  }

  @Test
  fun `add new user when it does not exist`(){

    given {userRepository.exist(newUser.username)} .willReturn(false)

    registerUser.create(signInDto)

    val  userCaptorAdd = argumentCaptor<User>()

    verify(userRepository).add(userCaptorAdd.capture())

    assertEquals(newUser.username,userCaptorAdd.firstValue.username)
    assertEquals(newUser.password,userCaptorAdd.firstValue.password)

  }

  @Test
  fun `create new user with default ROLE= ROLE_USER`(){

    given {userRepository.exist(newUser.username)} .willReturn(false)

    registerUser.create(signInDto)

    val  userCaptorAdd = argumentCaptor<User>()

    verify(userRepository).add(userCaptorAdd.capture())

    assertEquals(newUser.getRoles().elementAt(0).name, userCaptorAdd.firstValue.getRoles().elementAt(0).name)

  }




}
