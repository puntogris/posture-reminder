package com.puntogris.posture.utils

import android.annotation.SuppressLint
import android.content.res.ColorStateList
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.getColor
import androidx.databinding.BindingAdapter
import com.db.williamchart.view.DonutChartView
import com.google.android.material.button.MaterialButton
import com.puntogris.posture.R
import com.puntogris.posture.domain.model.Reminder
import com.puntogris.posture.utils.constants.Constants.EXPERIENCE_PER_LEVEL
import com.puntogris.posture.utils.constants.Constants.PROGRESS_BAR_SMOOTH_OFFSET
import org.threeten.bp.LocalDate
import org.threeten.bp.format.DateTimeFormatter

@BindingAdapter("exerciseDuration")
fun TextView.setExerciseDuration(duration: Int) {
    text = context.getString(R.string.exercise_duration_seconds, duration)
}

@BindingAdapter("minutesToHourlyTime")
fun TextView.setMinutesToHourlyTime(minutes: Int) {
    text = Utils.minutesFromMidnightToHourlyTime(minutes)
}

@BindingAdapter("daysSummary")
fun TextView.setDaysSummary(reminder: Reminder?) {
    text = reminder?.alarmDaysSummary(resources.getStringArray(R.array.alarmDays))
}

@BindingAdapter("reminderColor")
fun View.setReminderColor(color: Int) {
    backgroundTintList = try {
        ColorStateList.valueOf(getColor(context, color))
    } catch (e: Exception) {
        ColorStateList.valueOf(color)
    }
}

@BindingAdapter("dayMonth")
fun TextView.setDayMonth(position: Int) {
    var date = LocalDate.now()
    if (position == 1) date = date.minusDays(1)
    text = date.format(DateTimeFormatter.ofPattern("d MMM"))
}

@BindingAdapter("pagerDay")
fun TextView.setPagerDay(position: Int) {
    setText(if (position == 0) R.string.today_pager else R.string.yesterday_pager)
}

@BindingAdapter("accountLevelTitle")
fun TextView.setAccountLevelTitle(exp: Int) {
    text = context.getString(R.string.account_level_title, exp.getLevel())
}

@BindingAdapter("accountBadgeLevel")
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

@BindingAdapter("expForNextLevel")
fun TextView.setExpForNextLevel(exp: Int) {
    val nextLevel = exp.getLevel().inc()
    text = context.getString(R.string.experience_for_level_x, exp.expForNextLevel(), nextLevel)
}

@SuppressLint("SetTextI18n")
@BindingAdapter("expFromTotalLevel")
fun TextView.setExpFromTotalLevel(exp: Int) {
    text = "${exp.expForCompleteLevel()} / $EXPERIENCE_PER_LEVEL"
}

@BindingAdapter("donutChartProgress")
fun DonutChartView.setDonutChartProgress(exp: Int) {
    donutTotal = EXPERIENCE_PER_LEVEL.toFloat()
    donutColors = intArrayOf(getColor(context, R.color.colorPrimaryDark))
    animation.duration = 1000
    animate(listOf(exp.expForCompleteLevel().toFloat()))
}

@BindingAdapter("donutLevel")
fun TextView.setDonutLevel(exp: Int) {
    text = context.getString(R.string.account_donut_level, exp.getLevel())
}

@BindingAdapter("rankingLevel")
fun TextView.setRankingLevel(exp: Int) {
    text = context.getString(R.string.ranking_level, exp.getLevel())
}

@BindingAdapter("profileRankingNumber")
fun TextView.setProfileRankingNumber(position: Int) {
    if (position in 0..2) gone()
    else {
        visible()
        text = position.inc().toString()
    }
}

@BindingAdapter("profileRankingMedal")
fun ImageView.setProfileRankingMedal(position: Int) {
    when (position) {
        0 -> R.drawable.ic_gold_medal
        1 -> R.drawable.ic_silver_medal
        2 -> R.drawable.ic_bronze_medal
        else -> null
    }.let {
        if (it != null) {
            visible()
            setImageDrawable(ContextCompat.getDrawable(context, it))
        } else gone()
    }
}

@BindingAdapter("backgroundColorTintView")
fun View.setBackgroundColorTintView(color: Int) {
    backgroundTintList = try {
        ColorStateList.valueOf(getColor(context, color))
    } catch (e: Exception) {
        ColorStateList.valueOf(color)
    }
}

@BindingAdapter("progressBarSmoothMax")
fun ProgressBar.setProgressBarSmoothMax(duration: Int) {
    max = duration * PROGRESS_BAR_SMOOTH_OFFSET
}

@BindingAdapter("userRankingName")
fun TextView.setUserRankingName(name: String) {
    val list = name
        .split(" ")
        .filter { it.isNotBlank() }
        .map { it.replace(" ", "") }

    text = if (list.size == 1) {
        list.first().let { if (it.length < 25) it else it.substring(0, 25) + ".." }
    } else {
        var newName = ""
        var i = 0

        while (newName.length < 25 && i < list.size && (newName + list[i]).length < 25) {
            newName += if (newName.isEmpty()) list[i] else " " + list[i]
            i++
        }
        newName
    }
}

@BindingAdapter("toggleButton")
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
