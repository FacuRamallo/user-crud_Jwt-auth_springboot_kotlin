package es.adevinta.spain.friends.domain.exceptions

class InvalidPasswordException(message: String = "Password must contain from 8 to 12 alphanumerical characters") : UserDetailsException(message)
