package com.mansao.trianglesneacare.utils

object CommonUtils {
     fun isEmailValid(email: String): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }
}