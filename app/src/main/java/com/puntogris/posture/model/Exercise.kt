package com.puntogris.posture.model

import android.os.Parcelable
import androidx.annotation.ArrayRes
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.puntogris.posture.R
import kotlinx.parcelize.Parcelize

@Parcelize
class Exercise(
    @StringRes val title: Int,
    @StringRes val summary: Int,
    val duration: Int,
    @ColorRes val color: Int = R.color.colorPrimary,
    @DrawableRes val image: Int,
    @ArrayRes val steps: Int
):Parcelable