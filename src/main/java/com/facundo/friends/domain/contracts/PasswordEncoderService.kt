package com.facundo.friends.domain.contracts

interface PasswordEncoderService {

  fun encodePassword(value: String): String

}
