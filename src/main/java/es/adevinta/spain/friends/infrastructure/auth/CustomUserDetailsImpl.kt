package es.adevinta.spain.friends.infrastructure.auth

import es.adevinta.spain.friends.domain.User
import java.util.stream.Collectors
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

      val authorities: List<GrantedAuthority> = user.roles.stream()
        .map { role -> SimpleGrantedAuthority(role.getRoleName()) }
        .collect(Collectors.toList())

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
    val user: CustomUserDetailsImpl =
      other as CustomUserDetailsImpl
    return userName == user.userName
  }

  override fun hashCode(): Int {
    var result = serialVersionUID.hashCode()
    result = 31 * result + userName.hashCode()
    result = 31 * result + passWord.hashCode()
    result = 31 * result + authorities.hashCode()
    result = 31 * result + isAccountNonExpired.hashCode()
    result = 31 * result + isAccountNonLocked.hashCode()
    result = 31 * result + isCredentialsNonExpired.hashCode()
    result = 31 * result + isEnabled.hashCode()
    return result
  }
}
