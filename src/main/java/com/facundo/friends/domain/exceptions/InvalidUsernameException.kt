package com.facundo.friends.domain.exceptions

class InvalidUsernameException(message: String = "User name must contain from 5 to 10 alphanumerical characters") : UserDetailsException(message)
