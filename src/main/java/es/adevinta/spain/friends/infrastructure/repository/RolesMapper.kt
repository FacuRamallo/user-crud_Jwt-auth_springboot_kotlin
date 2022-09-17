package es.adevinta.spain.friends.infrastructure.repository

import es.adevinta.spain.friends.domain.Role

interface RolesMapper {

  fun saveRoles(username: String, roles: Set<Role>?)
  fun getRoles(username: String) : Set<Role>

}
