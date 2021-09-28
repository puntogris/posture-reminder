package com.puntogris.posture.utils

import androidx.annotation.Keep

@Keep
object Constants {

    //Experience
    const val EXPERIENCE_PER_EXERCISE = 10
    const val EXPERIENCE_PER_NOTIFICATION = 10
    const val EXPERIENCE_PER_LEVEL = 1000
    const val MAX_EXPERIENCE_PER_DAY = 100
    const val MAX_EXPERIENCE_OFFSET = 200

    //Room
    const val ROOM_DATABASE_NAME = "posture_reminder_database"

    //Other
    const val PROGRESS_BAR_SMOOTH_OFFSET = 20
    const val PACKAGE_URI_NAME = "package:com.puntogris.posture"

    //Notifications
    const val DAILY_ALARM_TRIGGERED = "DAILY_ALARM_TRIGGERED"
    const val REPEATING_ALARM_TRIGGERED = "REPEATING_ALARM_TRIGGERED"
    const val FMC_CHANNEL_ID = "fmc_channel"
    const val URI_STRING = "uriString"
    const val WEBSITE_HTTPS = "https://"
    const val CLAIM_NOTIFICATION_EXP_INTENT = "claim_notification_exp_intent"
    const val NAVIGATION_DATA = "navigation_data"
    const val NOTIFICATION_ID = "notification_id"

    //Keys
    const val TIME_UNIT_KEY = "time_unit_key"
    const val INTERVAL_KEY = "interval_key"
    const val DATA_KEY = "data_key"
    const val VIBRATION_PICKER_KEY = "vibration_picker_key"
    const val SOUND_PICKER_KEY = "sound_picker_key"
    const val SEND_TICKET_KEY = "send_ticket_key"
    const val EDIT_NAME_KEY = "edit_name_key"

    //Data Store
    const val APP_PREFERENCES_NAME = "app_preferences"
    const val LAST_VERSION_CODE = "last_version_code"
    const val APP_THEME = "pref_app_theme"
    const val PANDA_ANIMATION = "pref_panda_animation"
    const val REMINDER_STATE_KEY = "reminder_state_key"
    const val SHOW_LOGIN_KEY = "show_login_key"

    //Pref keys
    const val USERNAME_PREF_KEY = "username_preference_key"
    const val THEME_PREF_KEY = "theme_preference_key"
    const val BATTERY_PREF_KEY = "battery_preference_key"
    const val LOG_OUT_PREF_KEY = "log_out_preference_key"
    const val LOG_IN_PREF_KEY = "log_in_preference_key"
    const val CREDITS_PREF_KEY = "credits_preference_key"
    const val TICKET_PREF_KEY = "ticket_preference_key"
    const val VERSION_PREF_KEY = "version_preference_key"
    const val RATE_APP_PREF_KEY = "rate_app_preference_key"
    const val LICENSES_PREF_KEY = "licenses_preference_key"

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