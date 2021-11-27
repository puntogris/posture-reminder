package com.puntogris.posture.common.alarm

import com.puntogris.posture.feature_main.domain.model.Reminder

sealed class AlarmStatus {
    class Activated(val reminder: Reminder) : AlarmStatus()
    object Canceled : AlarmStatus()
    object NotConfigured : AlarmStatus()
}
