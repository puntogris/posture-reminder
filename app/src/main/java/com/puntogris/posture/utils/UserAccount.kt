package com.puntogris.posture.utils

sealed class UserAccount {
    object New : UserAccount()
    object Registered : UserAccount()
}