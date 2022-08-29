package es.adevinta.spain.friends.domain.exceptions

class NameAlreadyExistException(value: String) : UserDetailsException("$value username already exist")
