package com.puntogris.posture.common.utils

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