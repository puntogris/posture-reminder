package com.puntogris.posture.utils.extensions

import android.content.BroadcastReceiver
import android.view.Menu
import com.google.firebase.Timestamp
import com.puntogris.posture.utils.constants.Constants.EXPERIENCE_PER_LEVEL
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.threeten.bp.LocalDate
import org.threeten.bp.format.DateTimeFormatter
import timber.log.Timber
import java.util.*

fun Int.getHours() = this / 60

fun Int.getMinutes() = this % 60

fun Long.millisToTimestamp() = Timestamp(this / 100, 0)

fun Long.millisToMinutes() = (this / 1000 / 60).toInt()

fun Int.minutesToMillis() = this * 1000 * 60

fun Int.toMillis() = (this * 1000).toLong()

fun Int.getLevel() = (this / EXPERIENCE_PER_LEVEL) + 1

fun Int.expForNextLevel() = EXPERIENCE_PER_LEVEL - this % EXPERIENCE_PER_LEVEL

fun Int.expForCompleteLevel() = this % EXPERIENCE_PER_LEVEL

fun Long.toDays() = (this / (1000 * 60 * 60 * 24)).toInt()

fun String.capitalizeFirstChar(): String {
    return replaceFirstChar {
        if (it.isLowerCase()) it.titlecase(Locale.getDefault())
        else it.toString()
    }
}

fun String.capitalizeWords(): String {
    return split(" ").joinToString(" ") { it.capitalizeFirstChar() }
}

fun LocalDate.getDayName(): String {
    return format(DateTimeFormatter.ofPattern("EEE "))
        .replace(".", "")
        .capitalizeFirstChar()
}

fun Menu.showItem(item: Int) {
    findItem(item).isVisible = true
}

@DelicateCoroutinesApi
fun BroadcastReceiver.goAsync(
    coroutineScope: CoroutineScope = GlobalScope,
    block: suspend () -> Unit
) {
    val result = goAsync()
    coroutineScope.launch {
        try {
            block()
        } catch (e: Exception) {
            Timber.d(e.message.toString())
        } finally {
            result?.finish()
        }
    }
}

inline val Date.timeWithZoneOffset
    get() = time + TimeZone.getDefault().getOffset(time)

fun Any.notEqualsAny(vararg values: Any): Boolean {
    return this !in values
}