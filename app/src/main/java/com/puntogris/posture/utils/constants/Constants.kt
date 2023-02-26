package com.puntogris.posture.utils.constants

object Constants {

    //Experience
    const val EXPERIENCE_PER_EXERCISE = 10
    const val EXPERIENCE_PER_NOTIFICATION = 10
    const val EXPERIENCE_PER_LEVEL = 1000
    const val MAX_EXPERIENCE_PER_DAY = 100
    const val BASE_DATE_MILLIS = 1633126950000 //Friday, 1 October 2021 22:22:30

    //Room
    const val ROOM_DATABASE_NAME = "posture_reminder_database"

    //Other
    const val PROGRESS_BAR_SMOOTH_OFFSET = 20
    const val PACKAGE_URI_NAME = "package:com.puntogris.posture"
    const val ONE_SECOND_IN_MILLIS = 1000

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
    const val DATA_KEY = "data_key"
    const val VIBRATION_PICKER_KEY = "vibration_picker_key"
    const val SOUND_PICKER_KEY = "sound_picker_key"
    const val EDIT_NAME_KEY = "edit_name_key"

    //Work manager
    const val SYNC_ACCOUNT_WORKER = "sync_account_worker"
    const val REMINDER_ID_WORKER_DATA = "upload_reminder_worker"

    //Firestore
    const val EXPERIENCE_FIELD = "experience"
    const val PUBLIC_PROFILE_COL_GROUP = "public_profile"
    const val USERS_COLLECTION = "users"
    const val REMINDERS_COLLECTION = "reminders"
    const val PUBLIC_PROFILE_DOC = "profile"
    const val USER_NAME_FIELD = "username"
    const val TICKET_COLLECTION = "tickets"

    //Url
    const val AUTH_HELP_URL = "https://postureapp.puntogris.com/help/"

    //Exercise
    const val EXERCISE_KEY = "exercise"
    const val DEFAULT_EXERCISE_TIMER_SECS = 50

    //Flow for navigating battery optimization and permissions screen
    const val WELCOME_FLOW = "welcome_flow"
    const val SETTINGS_FLOW = "settings_flow"
}