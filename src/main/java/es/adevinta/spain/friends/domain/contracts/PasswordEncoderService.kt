package es.adevinta.spain.friends.domain.contracts

import es.adevinta.spain.friends.domain.PassWord

interface PasswordEncoderService {

  fun encodePassword(value: PassWord): String
}
