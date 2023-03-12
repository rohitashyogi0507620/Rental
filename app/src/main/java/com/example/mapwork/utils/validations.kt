package com.example.mapwork.utils

import java.util.regex.Matcher
import java.util.regex.Pattern


fun String.isEmailValid(): Boolean {
    return this.isNotEmpty() && android.util.Patterns.EMAIL_ADDRESS.matcher(this).matches()
}


fun String.isValidPanCard(): Boolean {
    val regex = "[A-Z]{5}[0-9]{4}[A-Z]{1}"
    val p = Pattern.compile(regex)
    val m: Matcher = p.matcher(this)
    return this.isNotEmpty() && m.matches()
}

fun String.isValidAadharCard(): Boolean {

    val regex = "^[2-9]{1}[0-9]{3}\\s[0-9]{4}\\s[0-9]{4}$"

    val p = Pattern.compile(regex)
    val m: Matcher = p.matcher(this)
    return this.isNotEmpty() && m.matches()


}

fun String.isValidMobileNo(): Boolean {
    val regex = "^(?:(?:\\+|0{0,2})91(\\s*[\\-]\\s*)?|[0]?)?[789]\\d{9}\$\n"
    val p = Pattern.compile(regex)
    val m: Matcher = p.matcher(this)
    return this.isNotEmpty() && m.matches()


}

fun String.isValidPassword(): Boolean {
    return if (length in 8..32) {
        //Contains small letter, Capital letter, a number
        val textPattern = Pattern.compile("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).+$")
        //Contains special character
        val special = Pattern.compile("[!@#$%&*()_+=|<>?{}\\[\\]~-]")
        textPattern.matcher(this).matches() && special.matcher(this).find()
    } else {
        false
    }
}


fun String.isValidMobile(): Boolean {
    return Pattern.matches("[0-9]+", this) && this.trim().isNotEmpty()
}


fun String.isValidName(): Boolean {
    return Pattern.matches("[a-zA-Z]+", this) && length > 0 && trim().isNotEmpty()
}