package com.puntogris.posture.utils

import android.annotation.SuppressLint
import android.content.res.ColorStateList
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.getColor
import androidx.core.view.isVisible
import com.db.williamchart.view.BarChartView
import com.db.williamchart.view.DonutChartView
import com.google.android.material.button.MaterialButton
import com.puntogris.posture.R
import com.puntogris.posture.domain.model.DayLog
import com.puntogris.posture.domain.model.Reminder
import com.puntogris.posture.utils.constants.Constants.EXPERIENCE_PER_LEVEL
import com.puntogris.posture.utils.constants.Constants.PROGRESS_BAR_SMOOTH_OFFSET
import com.puntogris.posture.utils.extensions.*
import org.threeten.bp.LocalDate
import org.threeten.bp.format.DateTimeFormatter

fun TextView.setText(stringRes: Int, vararg args: Any?) {
    text = context.getString(stringRes, *args)
}

fun TextView.setExerciseDuration(duration: Int) {
    text = context.getString(R.string.exercise_duration_seconds, duration)
}

fun TextView.setMinutesToHourlyTime(minutes: Int) {
    text = Utils.minutesFromMidnightToHourlyTime(minutes)
}

fun TextView.setDaysSummary(reminder: Reminder?) {
    text = reminder?.alarmDaysSummary(resources.getStringArray(R.array.alarmDays))
}

fun View.setReminderColor(color: Int) {
    backgroundTintList = try {
        ColorStateList.valueOf(getColor(context, color))
    } catch (e: Exception) {
        ColorStateList.valueOf(color)
    }
}

fun TextView.setDayMonth(position: Int) {
    var date = LocalDate.now()
    if (position == 1) date = date.minusDays(1)
    text = date.format(DateTimeFormatter.ofPattern("d MMM"))
}

fun TextView.setPagerDay(position: Int) {
    setText(if (position == 0) R.string.today_pager else R.string.yesterday_pager)
}

fun TextView.setAccountLevelTitle(exp: Int) {
    text = context.getString(R.string.account_level_title, exp.getLevel())
}

fun TextView.setAccountBadgeLevel(exp: Int) {
    val string = when (exp.getLevel()) {
        1 -> R.string.level_1_title
        2 -> R.string.level_2_title
        3 -> R.string.level_3_title
        4 -> R.string.level_4_title
        5 -> R.string.level_5_title
        6 -> R.string.level_6_title
        else -> R.string.level_7_title
    }
    setText(string)
}

fun TextView.setExpForNextLevel(exp: Int) {
    val nextLevel = exp.getLevel().inc()
    text = context.getString(R.string.experience_for_level_x, exp.expForNextLevel(), nextLevel)
}

@SuppressLint("SetTextI18n")
fun TextView.setExpFromTotalLevel(exp: Int) {
    text = "${exp.expForCompleteLevel()} / $EXPERIENCE_PER_LEVEL"
}

fun DonutChartView.setDonutChartProgress(exp: Int) {
    donutTotal = EXPERIENCE_PER_LEVEL.toFloat()
    donutColors = intArrayOf(getColor(context, R.color.colorPrimaryDark))
    animation.duration = 1000
    animate(listOf(exp.expForCompleteLevel().toFloat()))
}

fun TextView.setDonutLevel(exp: Int) {
    text = context.getString(R.string.account_donut_level, exp.getLevel())
}

fun TextView.setRankingLevel(exp: Int) {
    text = context.getString(R.string.ranking_level, exp.getLevel())
}

fun TextView.setProfileRankingNumber(position: Int) {
    if (position in 0..2) gone()
    else {
        visible()
        text = position.inc().toString()
    }
}

fun ImageView.setProfileRankingMedal(position: Int) {
    when (position) {
        0 -> R.drawable.ic_gold_medal
        1 -> R.drawable.ic_silver_medal
        2 -> R.drawable.ic_bronze_medal
        else -> null
    }.let {
        isVisible = it != null
        if (it != null) setImageDrawable(ContextCompat.getDrawable(context, it))
    }
}

fun View.setBackgroundColorTintView(color: Int) {
    backgroundTintList = try {
        ColorStateList.valueOf(getColor(context, color))
    } catch (e: Exception) {
        ColorStateList.valueOf(color)
    }
}

fun ProgressBar.setProgressBarSmoothMax(duration: Int) {
    max = duration * PROGRESS_BAR_SMOOTH_OFFSET
}

fun MaterialButton.setToggleButton(isReminderActive: Boolean) {
    val (color, text) =
        if (isReminderActive) {
            R.color.turn_off to R.string.action_off
        } else {
            R.color.turn_on to R.string.action_on
        }
    setBackgroundColor(getColor(context, color))
    setText(text)
}

fun BarChartView.setBarChartLabels(data: List<DayLog>?) {
    if (data == null) return
    val today = LocalDate.now()
    val labels = mutableListOf<Pair<String, Float>>()

    for (i in 6 downTo 0L) {
        val day = today.minusDays(i)
        val dayName = if (i == 0L) context.getString(R.string.today) else day.getDayName()

        val dayLog = data.singleOrNull { it.dateId == day.toString() }
        val expValue = dayLog?.expGained?.toFloat() ?: 0F
        labels.add(dayName to expValue)
    }
    animate(labels)
}

fun TextView.setUsernameOrPlaceHolder(name: String?) {
    text = if (name.isNullOrEmpty()) context.getString(R.string.human) else name
}