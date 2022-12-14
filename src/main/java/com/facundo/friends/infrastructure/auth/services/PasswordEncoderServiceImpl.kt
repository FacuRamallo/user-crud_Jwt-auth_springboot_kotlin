package com.facundo.friends.infrastructure.auth.services

import com.facundo.friends.domain.contracts.PasswordEncoderService
import org.springframework.security.crypto.password.PasswordEncoder



open class PasswordEncoderServiceImpl(private val passwordEncoder: PasswordEncoder) : PasswordEncoderService {

  override fun encodePassword(value: String): String = passwordEncoder.encode(value)
}

