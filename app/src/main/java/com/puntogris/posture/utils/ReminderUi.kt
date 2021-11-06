package com.puntogris.posture.utils

import com.puntogris.posture.R
import com.puntogris.posture.domain.model.ItemData

sealed class ReminderUi {
    sealed class Item(val itemData: ItemData) : ReminderUi() {
        class Interval(itemData: ItemData) : Item(itemData)
        class Start(itemData: ItemData) : Item(itemData)
        class End(itemData: ItemData) : Item(itemData)
        class Days(itemData: ItemData) : Item(itemData)
        class Sound(itemData: ItemData) : Item(itemData)
        class Vibration(itemData: ItemData) : Item(itemData)
    }

    class Name(var value: String = "") : ReminderUi()
    class Color(var color: Int = R.color.grey) : ReminderUi()
}
