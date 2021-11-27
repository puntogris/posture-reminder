package com.puntogris.posture.feature_main.domain.repository

import com.puntogris.posture.feature_main.domain.model.Reminder

interface ReminderServerApi {

    suspend fun deleteReminder(reminderId: String)

    suspend fun insertReminder(reminder: Reminder)

    suspend fun insertReminders(reminders: List<Reminder>)

    suspend fun getReminders(): List<Reminder>
}