package com.puntogris.posture.utils

import com.puntogris.posture.R

sealed class Result<T> {
    data class Success<T>(val value: T) : Result<T>()
    data class Error<T>(val error: Int = R.string.snack_general_error) : Result<T>()
    class Loading<T>: Result<T>()

    companion object Factory {
        inline fun <T> build(function: () -> T): Result<T> =
            try {
                Success(function.invoke())
            } catch (e: Exception) {
                Error()
            }
    }
}
