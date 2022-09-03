package es.adevinta.spain.friends.services

import es.adevinta.spain.friends.domain.PassWord
import org.springframework.security.crypto.password.PasswordEncoder



class PasswordEncoderService(private val passwordEncoder: PasswordEncoder)  {

  fun encodePassword(value: PassWord): String = passwordEncoder.encode(value.value)



}

