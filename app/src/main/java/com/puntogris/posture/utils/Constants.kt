package com.puntogris.posture.utils

object Constants {
    // 720 are minutes after midnight, so 12am
    const val DEFAULT_START_TIME_PERIOD = 720L
    // 0 are minutes after midnight, so 12pm
    const val DEFAULT_END_TIME_PERIOD = 0L
    const val DEFAULT_INTERVAL_REPEATING = 60L
    const val DEFAULT_SHOW_NOTIFICATIONS_PREF_VALUE = true
    const val DAILY_ALARM_TRIGGERED = "DAILY_ALARM_TRIGGERED"
    const val REPEATING_ALARM_TRIGGERED = "REPEATING_ALARM_TRIGGERED"
    const val POSTURE_NOTIFICATION_ID = "postureNotification"
    const val BUG_REPORT_COLLECTION_NAME = "bug_reports"
    const val START_TIME_PREFERENCE = "start_notification_time"
    const val END_TIME_PREFERENCE = "end_notification_time"
    const val TIME_INTERVAL_PREFERENCE = "time_interval_for_notifications"
    const val PANDA_ANIMATION_PREFERENCE = "panda_animation"
    const val BUG_REPORT_PREFERENCE = "bug_report"
    const val ALARM_DAYS_PREFERENCE= "alarm_days_notification"

}