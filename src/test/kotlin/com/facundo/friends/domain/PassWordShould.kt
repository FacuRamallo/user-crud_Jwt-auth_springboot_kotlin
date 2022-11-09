package com.facundo.friends.domain

import com.facundo.friends.domain.exceptions.InvalidPasswordException
import kotlin.test.assertFailsWith
import org.junit.jupiter.api.Test

class PassWordShould {

  @Test
  fun `fail when password does not contain only hexadec chars`(){
    val nonExadecPassword = "Abc.?=1234"

    assertFailsWith<InvalidPasswordException> {
      PassWord(nonExadecPassword)
    }
  }

  @Test
  fun `fail when password exceeds from 12 chars`(){
    val longPassword = "123456789abcd"

    assertFailsWith<InvalidPasswordException> {
      PassWord(longPassword)
    }
  }

  @Test
  fun `fail when password is shorter than 8 chars`(){
    val shortPassword = "1234567"

    assertFailsWith<InvalidPasswordException> {
      PassWord(shortPassword)
    }
  }

}
