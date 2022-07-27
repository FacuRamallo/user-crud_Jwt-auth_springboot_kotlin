package es.adevinta.spain.friends.domain.exceptions

class NameAlreadyExistException(value: String) : Exception("$value already exist")
