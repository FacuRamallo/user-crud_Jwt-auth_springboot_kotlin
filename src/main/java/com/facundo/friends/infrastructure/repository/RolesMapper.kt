package com.facundo.friends.infrastructure.repository

import com.facundo.friends.domain.Role

interface RolesMapper {

  fun saveRoles(username: String, roles: Set<Role>?)
  fun getRoles(username: String) : Set<Role>

}
