package com.puntogris.posture.utils

import com.puntogris.posture.R
import com.puntogris.posture.model.ItemData
import com.puntogris.posture.model.UserPrivateData

sealed class LoginResult {
    object InProgress : LoginResult()
    class Success(val userPrivateData: UserPrivateData) : LoginResult()
    object Error : LoginResult()
}

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

sealed class UserAccount {
    object New : UserAccount()
    object Registered : UserAccount()
}

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

sealed class RewardExp {
    object Success : RewardExp()
    object Error : RewardExp()
    object ExpLimit : RewardExp()
}

sealed class ReminderUi {
    sealed class Item(val itemData: ItemData) : ReminderUi() {
        class Interval(itemData: ItemData) : Item(itemData)
        class Start(itemData: ItemData) : Item(itemData)
        class End(itemData: ItemData) : Item(itemData)
        class Days(itemData: ItemData) : Item(itemData)
        class Sound(itemData: ItemData) : Item(itemData)
        class Vibration(itemData: ItemData) : Item(itemData)
    }

    class Name(var value: String = "") : ReminderUi()
    class Color(var color: Int = R.color.grey) : ReminderUi()
}
