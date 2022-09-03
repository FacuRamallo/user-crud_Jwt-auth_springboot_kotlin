package es.adevinta.spain.friends.domain

import java.util.UUID
import kotlin.String

data class UserID(val value: UUID = UUID.randomUUID()) {
  constructor(id: String) : this(UUID.fromString(id))
}
