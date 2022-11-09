package com.facundo.friends.domain

import com.facundo.friends.domain.exceptions.InvalidPasswordException
import java.util.regex.Matcher
import java.util.regex.Pattern
import kotlin.String


class PassWord(val value: String) {
  init {
    val regex = "^(?=.{8,12}\$)[a-zA-Z\\d]+$"
    val pattern : Pattern = Pattern.compile(regex)
    val matcher : Matcher = pattern.matcher(value)
    if(!matcher.matches()) throw InvalidPasswordException()
  }

}
