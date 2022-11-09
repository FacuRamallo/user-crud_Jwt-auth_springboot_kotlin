package com.facundo.friends.infrastructure.configuration

import com.facundo.friends.domain.contracts.UserRepository
import com.facundo.friends.infrastructure.auth.AuthEntryPointJwt
import com.facundo.friends.infrastructure.auth.AuthJwtFilter
import com.facundo.friends.infrastructure.auth.CustomUserDetailsServiceImpl
import com.facundo.friends.infrastructure.auth.JwtUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod.POST
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.UrlBasedCorsConfigurationSource
import org.springframework.web.filter.CorsFilter
import kotlin.Exception as JavaLangException

@Configuration
@EnableWebSecurity
class SecurityConfig @Autowired constructor(
  userRepository: UserRepository,
  unauthorizedHandler : AuthEntryPointJwt,
): WebSecurityConfigurerAdapter() {
  private val userRepository: UserRepository
  private val unauthorizedHandler: AuthEntryPointJwt

  init {
      this.userRepository = userRepository
      this.unauthorizedHandler = unauthorizedHandler
  }

  @Value("\${jwt.jwt-secret}")
  private lateinit var secret : String
  @Value("\${jwt.expiration-ms}")
  private lateinit var expiration : Number

  @Bean
  fun userDetailsService(userRepository: UserRepository) = CustomUserDetailsServiceImpl(userRepository)

  @Throws(Exception::class)
  override fun configure(authenticationManagerBuilder: AuthenticationManagerBuilder) {
    authenticationManagerBuilder
      .userDetailsService(userDetailsService(userRepository))
      .passwordEncoder(passwordEncoder())
  }

  @Override
  override fun configure(http: HttpSecurity){
    http.cors().and().csrf().disable()

    http
      .exceptionHandling()
      .authenticationEntryPoint(unauthorizedHandler).and()
      .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
      .and()
      .authorizeRequests()
      .antMatchers(POST, "/v1/authenticate", "/v1/signup").permitAll() // Our private endpoints
      .anyRequest().authenticated()

    http.addFilterBefore(
      authJwtFilter(jwtUtils(), userDetailsService(userRepository)),
      UsernamePasswordAuthenticationFilter::class.java
    )

  }

  @Bean
  fun corsFilter(): CorsFilter? {
    val source = UrlBasedCorsConfigurationSource()
    val config = CorsConfiguration()
    config.allowCredentials = true
    config.addAllowedOrigin("*")
    config.addAllowedHeader("*")
    config.addAllowedMethod("*")
    source.registerCorsConfiguration("/**", config)
    return CorsFilter(source)
  }

  @Bean
  @Throws(JavaLangException::class)
  override fun authenticationManagerBean(): AuthenticationManager? {
    return super.authenticationManagerBean()
  }

  @Bean
  fun passwordEncoder() = BCryptPasswordEncoder()

  @Bean
  fun jwtUtils() = JwtUtils(secret,expiration)

  @Bean
  fun authJwtFilter(jwtUtils: JwtUtils, userDetailsService: UserDetailsService) = AuthJwtFilter(jwtUtils,userDetailsService)

}
