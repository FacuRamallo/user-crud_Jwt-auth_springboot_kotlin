package es.adevinta.spain.friends.domain.exceptions

class UserNameNotFoundException(val username: String?) : Exception("UserName \"$username\" not found")
