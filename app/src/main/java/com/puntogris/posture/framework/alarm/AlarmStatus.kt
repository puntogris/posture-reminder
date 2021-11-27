package com.puntogris.posture.framework.alarm

import com.puntogris.posture.domain.model.Reminder

sealed class AlarmStatus {
    class Activated(val reminder: Reminder) : AlarmStatus()
    object Canceled : AlarmStatus()
    object NotConfigured : AlarmStatus()
}
