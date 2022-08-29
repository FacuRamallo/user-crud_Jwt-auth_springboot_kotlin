package es.adevinta.spain.friends.domain

import es.adevinta.spain.friends.domain.exceptions.InvalidUsernameException
import java.util.regex.Matcher
import java.util.regex.Pattern

data class UserName( val value: String) {
  init {
    val regex = "^(?=.{5,10}$)[a-zA-Z\\d]+$"
    val pattern : Pattern = Pattern.compile(regex)
    val matcher : Matcher = pattern.matcher(value)
    if(!matcher.matches()) throw InvalidUsernameException()
  }
}
