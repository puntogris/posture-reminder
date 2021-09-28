package com.puntogris.posture.alarm

import com.puntogris.posture.model.Reminder

sealed class AlarmStatus{
    class Activated(val reminder: Reminder): AlarmStatus()
    object Canceled: AlarmStatus()
    object NoConfigured: AlarmStatus()
}
