package es.adevinta.spain.friends.infrastructure.auth

import io.jsonwebtoken.ExpiredJwtException
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.MalformedJwtException
import io.jsonwebtoken.SignatureAlgorithm
import io.jsonwebtoken.UnsupportedJwtException
import io.jsonwebtoken.io.Decoders
import io.jsonwebtoken.security.Keys
import java.security.Key
import java.util.Date
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.security.core.Authentication

class JwtUtils(private val jwtSecret: String, private val jwtExpirationMs: Number) {
  private val logger: Logger = LoggerFactory.getLogger(this::class.java)


  private var keyBytes : ByteArray = Decoders.BASE64.decode(jwtSecret)
  private var key : Key = Keys.hmacShaKeyFor(keyBytes)

  fun generateJwt(authentication : Authentication ): String {
    val userPrincipal: CustomUserDetailsImpl = authentication.principal as CustomUserDetailsImpl

    return Jwts.builder()
      .setSubject(userPrincipal.username)
      .setIssuedAt(Date())
      .setExpiration(Date(Date().time + jwtExpirationMs.toInt()))
      .signWith(key, SignatureAlgorithm.HS512)
      .compact()
  }

  fun getUserNameFromJwt(token: String): String {
    return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).body.subject
  }

  fun validateJwt(authToken: String?): Boolean {
    try {
      Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(authToken)
      return true
    } catch (e: SecurityException) {
      logger.error("Invalid JWT signature: {}", e.message)
    } catch (e: MalformedJwtException) {
      logger.error("Invalid JWT token: {}", e.message)
    } catch (e: ExpiredJwtException) {
      logger.error("JWT token is expired: {}", e.message)
    } catch (e: UnsupportedJwtException) {
      logger.error("JWT token is unsupported: {}", e.message)
    } catch (e: IllegalArgumentException) {
      logger.error("JWT claims string is empty: {}", e.message)
    }
    return false
  }

}
