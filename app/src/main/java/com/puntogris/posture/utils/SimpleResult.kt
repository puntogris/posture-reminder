package com.puntogris.posture.utils

sealed class SimpleResult {
    object Success : SimpleResult()
    object Failure : SimpleResult()

    companion object Factory {
        inline fun build(function: () -> Unit): SimpleResult =
            try {
                function.invoke()
                Success
            } catch (e: Exception) {
                Failure
            }
    }
}

fun SimpleResult.isSuccess() = this == SimpleResult.Success
