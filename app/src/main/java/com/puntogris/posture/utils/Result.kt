package com.puntogris.posture.utils

sealed class Result<T> {
    data class Success<T>(val value: T) : Result<T>()
    data class Error<T>(val exception: Exception) : Result<T>()

    companion object Factory {
        inline fun <T> build(function: () -> T): Result<T> =
            try {
                Success(function.invoke())
            } catch (e: java.lang.Exception) {
                Error(e)
            }
    }
}
