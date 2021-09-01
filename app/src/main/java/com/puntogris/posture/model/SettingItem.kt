package com.puntogris.posture.model

class SettingItem(val code: SettingUi? = null, val title: Int, var summary: String= "")

enum class SettingUi{
    Name,
    Theme,
    BatteryOpt,
    LogOut,
    Help,
    Ticket,
    Website,
    Github,
    Credits,
    RateApp,
    Version
}