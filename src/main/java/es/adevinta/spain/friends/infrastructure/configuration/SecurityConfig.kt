package es.adevinta.spain.friends.infrastructure.configuration

import es.adevinta.spain.friends.auth.AuthEntryPointJwt
import es.adevinta.spain.friends.auth.AuthJwtFilter
import es.adevinta.spain.friends.auth.CustomUserDetailsServiceImpl
import es.adevinta.spain.friends.auth.JwtUtils
import es.adevinta.spain.friends.domain.contracts.UserRepository
import org.springframework.beans.factory.annotation.Autowired
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

  @Bean
  fun userDetailsService(userRepository: UserRepository) = CustomUserDetailsServiceImpl(userRepository)

  @Throws(Exception::class)
  override fun configure(authenticationManagerBuilder: AuthenticationManagerBuilder) {
    authenticationManagerBuilder.userDetailsService(userDetailsService(userRepository)).passwordEncoder(passwordEncoder())
  }

  @Override
  override fun configure(http: HttpSecurity){
    http.cors().and().csrf().disable()

    http
      .exceptionHandling()
      .authenticationEntryPoint(unauthorizedHandler).and()
      .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
      .authorizeRequests().antMatchers(POST, "/v1/**").permitAll() // Our private endpoints
      .anyRequest().authenticated()

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
  fun authJwtFilter(jwtUtils: JwtUtils, userDetailsService: UserDetailsService) = AuthJwtFilter(jwtUtils,userDetailsService)

}
