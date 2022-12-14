package com.facundo.friends.domain

import com.facundo.friends.domain.exceptions.InvalidUsernameException
import java.util.regex.Matcher
import java.util.regex.Pattern
import kotlin.String

data class UserName( val value: String) {
  init {
    val regex = "^(?=.{5,10}$)[a-zA-Z\\d]+$"
    val pattern : Pattern = Pattern.compile(regex)
    val matcher : Matcher = pattern.matcher(value)
    if(!matcher.matches()) throw InvalidUsernameException()
  }
}
