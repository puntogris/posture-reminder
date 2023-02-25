package com.puntogris.posture.utils

import android.content.res.ColorStateList
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.getColor
import androidx.core.view.isVisible
import com.db.williamchart.view.BarChartView
import com.db.williamchart.view.DonutChartView
import com.puntogris.posture.R
import com.puntogris.posture.domain.model.DayLog
import com.puntogris.posture.domain.model.Reminder
import com.puntogris.posture.utils.constants.Constants.EXPERIENCE_PER_LEVEL
import com.puntogris.posture.utils.constants.Constants.PROGRESS_BAR_SMOOTH_OFFSET
import com.puntogris.posture.utils.extensions.expForCompleteLevel
import com.puntogris.posture.utils.extensions.expForNextLevel
import com.puntogris.posture.utils.extensions.getDayName
import com.puntogris.posture.utils.extensions.getLevel
import org.threeten.bp.LocalDate
import org.threeten.bp.format.DateTimeFormatter

fun TextView.setText(stringRes: Int, vararg args: Any?) {
    text = context.getString(stringRes, *args)
}

fun setExerciseDuration(textView: TextView, duration: Int) {
    textView.setText(R.string.exercise_duration_seconds, duration)
}

fun setMinutesToHourlyTime(textView: TextView, minutes: Int) {
    textView.text = Utils.minutesFromMidnightToHourlyTime(minutes)
}

fun setDaysSummary(textView: TextView, reminder: Reminder?) {
    reminder?.let {
        textView.text = reminder.alarmDaysSummary(textView.resources.getStringArray(R.array.alarmDays))
    }
}

fun View.setReminderColor(color: Int) {
    backgroundTintList = try {
        ColorStateList.valueOf(getColor(context, color))
    } catch (e: Exception) {
        ColorStateList.valueOf(color)
    }
}

fun setDayMonth(textView: TextView, position: Int) {
    var date = LocalDate.now()
    if (position == 1) {
        date = date.minusDays(1)
    }
    textView.text = date.format(DateTimeFormatter.ofPattern("d MMM"))
}

fun setPagerDay(textView: TextView, position: Int) {
    textView.setText(if (position == 0) R.string.today_pager else R.string.yesterday_pager)
}

fun setAccountLevelTitle(textView: TextView, exp: Int) {
    textView.setText(R.string.account_level_title, exp.getLevel())
}

fun setAccountBadgeLevel(textView: TextView, exp: Int) {
    val string = when (exp.getLevel()) {
        1 -> R.string.level_1_title
        2 -> R.string.level_2_title
        3 -> R.string.level_3_title
        4 -> R.string.level_4_title
        5 -> R.string.level_5_title
        6 -> R.string.level_6_title
        else -> R.string.level_7_title
    }
    textView.setText(string)
}

fun setExpForNextLevel(textView: TextView, exp: Int) {
    textView.setText(
        R.string.experience_for_level_x,
        exp.expForNextLevel(),
        exp.getLevel().inc()
    )
}

fun setExpFromTotalLevel(textView: TextView, exp: Int) {
    textView.setText(
        R.string.numbers_with_slash_divider,
        exp.expForCompleteLevel(),
        EXPERIENCE_PER_LEVEL
    )
}

fun setDonutChartProgress(donutChartView: DonutChartView, exp: Int) {
    donutChartView.apply {
        donutTotal = EXPERIENCE_PER_LEVEL.toFloat()
        donutColors = intArrayOf(getColor(context, R.color.colorPrimaryDark))
        animation.duration = 1000
        animate(listOf(exp.expForCompleteLevel().toFloat()))
    }
}

fun setDonutLevel(textView: TextView, exp: Int) {
    textView.setText(R.string.account_donut_level, exp.getLevel())
}

fun setProfileRankingNumber(textView: TextView, position: Int) {
    textView.apply {
        if (position in 0..2) {
            isVisible = false
        } else {
            isVisible = true
            text = position.inc().toString()
        }
    }
}

fun setProfileRankingMedal(imageView: ImageView, position: Int) {
    when (position) {
        0 -> R.drawable.ic_gold_medal
        1 -> R.drawable.ic_silver_medal
        2 -> R.drawable.ic_bronze_medal
        else -> null
    }.let {
        imageView.isVisible = it != null
        if (it != null) {
            imageView.setImageDrawable(ContextCompat.getDrawable(imageView.context, it))
        }
    }
}

fun View.setBackgroundColorTintView(color: Int) {
    backgroundTintList = try {
        ColorStateList.valueOf(getColor(context, color))
    } catch (e: Exception) {
        ColorStateList.valueOf(color)
    }
}

fun setProgressBarSmoothMax(progressBar: ProgressBar, duration: Int) {
    progressBar.max = duration * PROGRESS_BAR_SMOOTH_OFFSET
}

fun setToggleButton(button: Button, isReminderActive: Boolean) {
    val (color, text) =
        if (isReminderActive) {
            R.color.turn_off to R.string.action_off
        } else {
            R.color.turn_on to R.string.action_on
        }
    button.setBackgroundColor(getColor(button.context, color))
    button.setText(text)
}

fun setBarChartLabels(barChartView: BarChartView, data: List<DayLog>?) {
    val today = LocalDate.now()
    val labels = mutableListOf<Pair<String, Float>>()

    for (i in 6 downTo 0L) {
        val logDate = today.minusDays(i)
        val dayName = if (i == 0L) {
            barChartView.context.getString(R.string.today)
        } else {
            logDate.getDayName()
        }
        val log = data?.find { it.dateId == logDate.toString() }
        val logExp = log?.expGained?.toFloat() ?: 0F

        labels.add(dayName to logExp)
    }
    barChartView.animate(labels)
}

fun setUsernameOrPlaceHolder(textView: TextView, name: String?) {
    if (name.isNullOrEmpty()) {
        textView.setText(R.string.human)
    } else {
        textView.text = name
    }
}