package es.adevinta.spain.friends.domain.exceptions

class TargetUserNameNotFoundException(val username: String) : Exception("User $username not found")
