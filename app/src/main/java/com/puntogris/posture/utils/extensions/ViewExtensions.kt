package com.puntogris.posture.utils.extensions

import android.widget.EditText
import androidx.annotation.RawRes
import androidx.viewpager2.widget.ViewPager2
import com.airbnb.lottie.LottieAnimationView
import kotlin.math.abs

fun ViewPager2.setPageFadeTransformer() {
    setPageTransformer { page, position ->
        page.alpha = when {
            position <= -1.0F || position >= 1.0F -> 0.0F
            position == 0.0F -> 1.0F
            else -> 1.0F - abs(position)
        }
    }
}

inline val EditText.data
    get() = text.toString()

fun LottieAnimationView.playAnimationOnce(@RawRes animation: Int) {
    setAnimation(animation)
    repeatCount = 0
    playAnimation()
}
