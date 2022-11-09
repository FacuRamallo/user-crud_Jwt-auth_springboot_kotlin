package com.facundo.friends.domain.exceptions

class NameAlreadyExistException(value: String) : UserDetailsException("$value username already exist")
