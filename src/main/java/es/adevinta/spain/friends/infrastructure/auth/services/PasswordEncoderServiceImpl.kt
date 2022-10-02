package es.adevinta.spain.friends.infrastructure.auth.services

import es.adevinta.spain.friends.domain.PassWord
import es.adevinta.spain.friends.domain.contracts.PasswordEncoderService
import org.springframework.security.crypto.password.PasswordEncoder



open class PasswordEncoderServiceImpl(private val passwordEncoder: PasswordEncoder) :
  PasswordEncoderService {

  override fun encodePassword(value: PassWord): String = passwordEncoder.encode(value.value)

}

