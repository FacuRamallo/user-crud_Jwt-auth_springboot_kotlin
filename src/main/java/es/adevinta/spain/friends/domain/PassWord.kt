package es.adevinta.spain.friends.domain

import es.adevinta.spain.friends.domain.exceptions.NonAlphanumericalCharacterException
import java.util.regex.Matcher
import java.util.regex.Pattern

data class PassWord(val value: String) {
  init {
    val regex = "^(?=.{8,12}\$)[a-zA-Z\\d]+$"
    val pattern : Pattern = Pattern.compile(regex)
    val matcher : Matcher = pattern.matcher(value)
    if(!matcher.matches()) throw NonAlphanumericalCharacterException()
  }
}
