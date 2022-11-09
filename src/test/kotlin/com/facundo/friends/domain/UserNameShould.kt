package com.facundo.friends.domain

import com.facundo.friends.domain.exceptions.InvalidUsernameException
import kotlin.test.assertFailsWith
import org.junit.jupiter.api.Test

class UserNameShould {

  @Test
  fun `fail when username does not contain only hexadec chars`(){
    val nonExadecUsername = "1234.?="

    assertFailsWith<InvalidUsernameException> {
      UserName(nonExadecUsername)
    }
  }

  @Test
  fun `fail when username exceeds from 10 chars`(){
    val longUsername = "123456789ab"

    assertFailsWith<InvalidUsernameException> {
      UserName(longUsername)
    }
  }

  @Test
  fun `fail when username is shorter than 5 chars`(){
    val shortUsername = "1234"

    assertFailsWith<InvalidUsernameException> {
      UserName(shortUsername)
    }
  }
}
