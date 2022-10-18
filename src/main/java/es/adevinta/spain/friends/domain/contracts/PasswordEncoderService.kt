package es.adevinta.spain.friends.domain.contracts

interface PasswordEncoderService {

  fun encodePassword(value: String): String

}
