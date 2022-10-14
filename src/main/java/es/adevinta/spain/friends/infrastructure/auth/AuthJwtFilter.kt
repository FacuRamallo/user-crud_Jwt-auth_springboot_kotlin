package es.adevinta.spain.friends.infrastructure.auth

import java.io.IOException
import javax.servlet.FilterChain
import javax.servlet.ServletException
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource
import org.springframework.util.StringUtils
import org.springframework.util.StringUtils.hasText
import org.springframework.web.filter.OncePerRequestFilter

class AuthJwtFilter(
  private val jwtUtils: JwtUtils,
  private val userDetailsService: UserDetailsService,
) : OncePerRequestFilter() {
  private val loggerA: Logger = LoggerFactory.getLogger(AuthJwtFilter::class.java)

  @Throws(ServletException::class, IOException::class)
  override fun doFilterInternal(request: HttpServletRequest, response: HttpServletResponse, filterChain: FilterChain) {
    try {
      val jwt: String? = parseJwt(request)

      if (!jwt.isNullOrBlank() && jwtUtils.validateJwt(jwt)) {
        val userName: String = jwtUtils.getUserNameFromJwt(jwt)

        val userDetails: UserDetails = userDetailsService.loadUserByUsername(userName)
        val authentication = UsernamePasswordAuthenticationToken(userDetails, null, userDetails.authorities)
          .apply { details = WebAuthenticationDetailsSource().buildDetails(request) }

        SecurityContextHolder.getContext().authentication = authentication
      }
    }catch (e : Exception ) {
      loggerA.error("Cannot set user authentication: {}", e);
    }

    filterChain.doFilter(request, response)
  }

  private fun parseJwt(request: HttpServletRequest): String? {
    val authHeader: String? = request.getHeader("Authorization")

    authHeader?.apply {
      if(hasText(this) && startsWith("Bearer ")){
        return this.substring(7)
      }
    }
    return null
  }

}
