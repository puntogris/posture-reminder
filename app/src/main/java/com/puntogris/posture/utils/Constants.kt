package com.puntogris.posture.utils

object Constants {

    const val DEFAULT_PANDA_ANIMATION_VALUE = false
    const val DEFAULT_APP_STATUS_PREF_VALUE = false
    // 720 are minutes after midnight, so 12am
    const val DEFAULT_START_TIME_PERIOD_NOTIFICATIONS_PREF_VALUE = 720
    // 0 are minutes after midnight, so 12pm
    const val DEFAULT_END_TIME_PERIOD_NOTIFICATIONS_PREF_VALUE = 0
    const val DEFAULT_INTERVAL_REPEATING_NOTIFICATIONS_PREF_VALUE = "60"
    const val DEFAULT_SHOW_NOTIFICATIONS_PREF_VALUE = true
    const val DAILY_ALARM_TRIGGERED = "DAILY_ALARM_TRIGGERED"
    const val REPEATING_ALARM_TRIGGERED = "REPEATING_ALARM_TRIGGERED"
    const val POSTURE_NOTIFICATION_ID = "postureNotification"
    const val BUG_REPORT_COLLECTION_NAME = "bug_reports"
    const val BUG_REPORT_DOCUMENT_KEY = "report"

}