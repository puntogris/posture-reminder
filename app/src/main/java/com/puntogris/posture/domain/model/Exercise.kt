package com.puntogris.posture.domain.model

import android.os.Parcelable
import androidx.annotation.*
import com.puntogris.posture.R
import kotlinx.parcelize.Parcelize

@Keep
@Parcelize
class Exercise(

    @StringRes val title: Int,

    @StringRes val summary: Int,

    val duration: Int,

    @ColorRes val color: Int = R.color.colorPrimary,

    @DrawableRes val image: Int,

    @ArrayRes val steps: Int

) : Parcelable