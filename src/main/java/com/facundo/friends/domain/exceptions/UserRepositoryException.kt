package com.facundo.friends.domain.exceptions

class UserRepositoryException(message: String, innerException: Throwable) : Exception(message, innerException)
