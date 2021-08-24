package com.puntogris.posture.utils

object Constants {

    //Experience
    const val EXPERIENCE_PER_EXERCISE = 10
    const val EXPERIENCE_PER_NOTIFICATION = 10
    const val EXPERIENCE_PER_LEVEL = 1000
    //Room
    const val ROOM_DATABASE_NAME = "posture_reminder_database"

    //Other
    const val SHAKE_ANIMATION_DURATION = 800L
    const val PROGRESS_BAR_SMOOTH_OFFSET = 20
    const val PACKAGE_URI_NAME = "package:com.puntogris.posture"

    //Notifications
    const val CHANNEL_NAME = "Posture Reminder Notification"
    const val DAILY_ALARM_TRIGGERED = "DAILY_ALARM_TRIGGERED"
    const val REPEATING_ALARM_TRIGGERED = "REPEATING_ALARM_TRIGGERED"

    //Keys
    const val TIME_UNIT_KEY = "time_unit_key"
    const val INTERVAL_KEY = "interval_key"
    const val DATA_KEY = "data_key"
    const val VIBRATION_PICKER_KEY = "vibration_picker_key"
    const val SOUND_PICKER_KEY = "sound_picker_key"
    const val SEND_TICKET_KEY = "send_ticket_key"
    const val EDIT_NAME_KEY = "edit_name_key"

    //Pref keys
    const val APP_PREFERENCES_NAME = "app_preferences"
    const val LAST_VERSION_CODE = "last_version_code"
    const val APP_THEME = "pref_app_theme"
    const val PANDA_ANIMATION = "pref_panda_animation"
    const val REMINDER_STATE_KEY = "reminder_state_key"

    //Work manager
    const val SYNC_ACCOUNT_WORKER = "sync_account_worker"
    const val REMINDER_ID_WORKER_DATA = "upload_reminder_worker"

    //Firestore
    const val EXPERIENCE_FIELD = "experience"
    const val PUBLIC_PROFILE_COL_GROUP = "public_profile"
    const val USERS_COLLECTION = "users"
    const val REMINDERS_COLLECTION = "reminders"
    const val PUBLIC_PROFILE_DOC = "profile"
    const val USER_NAME_FIELD = "name"
    const val TICKET_COLLECTION = "tickets"

}