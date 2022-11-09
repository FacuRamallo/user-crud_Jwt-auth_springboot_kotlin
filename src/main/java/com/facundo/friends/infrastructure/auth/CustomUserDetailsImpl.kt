package com.facundo.friends.infrastructure.auth

import com.facundo.friends.domain.User
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails

class CustomUserDetailsImpl private constructor(
  val serialVersionUID: Long,
  private val userName: String,
  private var passWord: String,
  private var authorities: List<GrantedAuthority>,
  private var isAccountNonExpired : Boolean,
  private var isAccountNonLocked : Boolean,
  private var isCredentialsNonExpired : Boolean,
  private var isEnabled: Boolean,
) : UserDetails {

  companion object {
    fun build(user: User): CustomUserDetailsImpl {

      val authorities: List<GrantedAuthority> = user.roles?.map { role -> SimpleGrantedAuthority(role.roleName) } ?: emptyList()

      return CustomUserDetailsImpl(
        1L,
        user.username.value,
        user.password,
        authorities,
        true,
        true,
        true,
        true
        )
    }
  }

  override fun getAuthorities(): List< GrantedAuthority> = authorities

  override fun getPassword(): String = passWord

  override fun getUsername(): String = userName

  override fun isAccountNonExpired(): Boolean = isAccountNonExpired

  override fun isAccountNonLocked(): Boolean = isAccountNonLocked

  override fun isCredentialsNonExpired(): Boolean = isCredentialsNonExpired

  override fun isEnabled(): Boolean = isEnabled

  override fun equals(other: Any?): Boolean {
    if (this === other) return true
    if (other == null || javaClass != other.javaClass) return false
    val user: CustomUserDetailsImpl = other as CustomUserDetailsImpl
    return userName == user.userName
  }

  override fun hashCode(): Int {
    var result = serialVersionUID.hashCode()
    result += userName.hashCode()
    result += passWord.hashCode()
    result += authorities.hashCode()
    result += isAccountNonExpired.hashCode()
    result += isAccountNonLocked.hashCode()
    result += isCredentialsNonExpired.hashCode()
    result += isEnabled.hashCode()
    return result
  }
}
