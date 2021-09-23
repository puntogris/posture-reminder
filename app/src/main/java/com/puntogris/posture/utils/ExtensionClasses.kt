package com.puntogris.posture.utils

import com.puntogris.posture.R
import com.puntogris.posture.model.ItemData
import com.puntogris.posture.model.Reminder
import com.puntogris.posture.model.UserPrivateData

sealed class SimpleResult{
    object Success: SimpleResult()
    object Failure: SimpleResult()
}

sealed class ReminderUi{
    sealed class Item(val itemData: ItemData): ReminderUi() {
        class Interval(itemData: ItemData): Item(itemData)
        class Start(itemData: ItemData): Item(itemData)
        class End(itemData: ItemData): Item(itemData)
        class Days(itemData: ItemData): Item(itemData)
        class Sound(itemData: ItemData): Item(itemData)
        class Vibration(itemData: ItemData): Item(itemData)
    }
    class Name(var value: String = ""): ReminderUi()
    class Color(var color: Int = R.color.grey): ReminderUi()
}

sealed class LoginResult {
    object InProgress: LoginResult()
    class Success(val userPrivateData: UserPrivateData): LoginResult()
    object Error: LoginResult()
}

sealed class UserAccount{
    object New: UserAccount()
    object Registered: UserAccount()
}

sealed class RepoResult<out T : Any> {
    data class Success<out T : Any>(val data: T) : RepoResult<T>()
    data class Error(val exception: Exception) : RepoResult<Nothing>()
}

sealed class AlarmStatus{
    class Activated(val reminder: Reminder): AlarmStatus()
    object Canceled: AlarmStatus()
    object NoConfigured: AlarmStatus()
}

sealed class RewardExp {
    object Success: RewardExp()
    object Error: RewardExp()
    object ExpLimit: RewardExp()
}

sealed class Result<out T : Any> {
    data class Success<out T : Any>(val data: T) : Result<T>()
    data class Error(val exception: Exception) : Result<Nothing>()
}