package es.adevinta.spain.friends.application

import com.nhaarman.mockito_kotlin.argumentCaptor
import com.nhaarman.mockito_kotlin.given
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.never
import com.nhaarman.mockito_kotlin.verify
import es.adevinta.spain.friends.domain.contracts.UserRepository
import es.adevinta.spain.friends.domain.exceptions.NameAlreadyExistException
import es.adevinta.spain.friends.domain.exceptions.NonAlphanumericalCharacterException
import es.adevinta.spain.friends.domain.PassWord
import es.adevinta.spain.friends.domain.User
import es.adevinta.spain.friends.domain.UserName
import org.junit.jupiter.api.Test
import es.adevinta.spain.friends.infrastructure.controller.dtos.NewUserDto
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class CreateUserShould {

  private val userRepository = mock<UserRepository>()

  private val newUserWhithWrongUserName = NewUserDto("usuario1+`´ç","abc1234")
  private val newUserWhithWrongPassword = NewUserDto("usuario1","abc1234`+ç´¡''¡¡")
  private val newUserDto = NewUserDto("usuarioOK","abcd1234")
  private val newUser = User(UserName("usuarioOK"),PassWord("abcd1234"))


  @Test
  fun `fail when userName contains non alphanumerical characters`(){
    val expectedMessage = "User Name and Password must contain only alphanumerical characters."

    val createUser = CreateUser(userRepository)
    val exception = assertFailsWith<NonAlphanumericalCharacterException> {
      createUser.execute(newUserWhithWrongUserName)
    }

    assertEquals(expectedMessage, exception.message)
    verify(userRepository, never()).add(newUser)
  }

  @Test
  fun `fail when password contains non alphanumerical characters`(){
    val expectedMessage = "User Name and Password must contain only alphanumerical characters."

    val createUser = CreateUser(userRepository)
    val exception = assertFailsWith<NonAlphanumericalCharacterException> {
      createUser.execute(newUserWhithWrongPassword)
    }

    assertEquals(expectedMessage, exception.message)
    verify(userRepository, never()).add(newUser)
  }

  @Test
  fun `fail when userName already exist`(){
    val expectedMessage = "usuarioOK already exist"

    given {userRepository.exist(newUser.username)} .willReturn(true)

    val createUser = CreateUser(userRepository)
    val exception = assertFailsWith<NameAlreadyExistException> {
      createUser.execute(newUserDto)
    }

    assertEquals(expectedMessage, exception.message)
    verify(userRepository, never()).add(newUser)

  }

  @Test
  fun `add new user when it does not exist`(){

    given {userRepository.exist(newUser.username)} .willReturn(false)

    val createUser = CreateUser(userRepository)

    createUser.execute(newUserDto)

    val  userCaptorAdd = argumentCaptor<User>()
    verify(userRepository).add(userCaptorAdd.capture())

    assertEquals(newUser,userCaptorAdd.firstValue)

  }


}
