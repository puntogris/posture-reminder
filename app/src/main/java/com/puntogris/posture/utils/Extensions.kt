package com.puntogris.posture.utils

import android.animation.ObjectAnimator
import android.app.Activity
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.net.Uri
import android.os.PowerManager
import android.util.Log
import android.view.Menu
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.annotation.RawRes
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.navigation.fragment.NavHostFragment
import androidx.viewpager2.widget.ViewPager2
import com.airbnb.lottie.LottieAnimationView
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.snackbar.Snackbar
import com.puntogris.posture.R
import com.puntogris.posture.ui.main.UiInterfaceListener
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.threeten.bp.LocalDate
import org.threeten.bp.format.DateTimeFormatter
import java.util.*
import kotlin.math.abs

fun View.gone() {
    visibility = View.GONE
}

fun View.visible() {
    visibility = View.VISIBLE
}

fun Int.getHours() = this / 60

fun Int.getMinutes() = this % 60

fun Long.millisToMinutes() = (this / 1000 / 60).toInt()

fun Int.minutesToMillis() = this * 1000 * 60

fun AppCompatActivity.getNavController() = getNavHostFragment().navController

fun AppCompatActivity.getNavHostFragment() =
    (supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment)

fun View.playShakeAnimation(){
    ObjectAnimator
        .ofFloat(this,"translationX", 0f, 25f, -25f, 25f, -25f,15f, -15f, 6f, -6f, 0f)
        .setDuration(Constants.SHAKE_ANIMATION_DURATION)
        .start()
}

fun Fragment.isDarkThemeOn() =
    (resources.configuration.uiMode and
            Configuration.UI_MODE_NIGHT_MASK == Configuration.UI_MODE_NIGHT_YES)

fun Menu.showItem(item: Int){
    findItem(item).isVisible = true
}

fun Fragment.isIgnoringBatteryOptimizations(): Boolean{
    val pm = requireContext().getSystemService(PowerManager::class.java)
    return (pm.isIgnoringBatteryOptimizations(requireActivity().packageName))
}

fun <T> MutableLiveData<T>.setField(transform: T.() -> Unit) { this.value = this.value?.apply(transform) }

fun ViewPager2.setPageFadeTransformer(){
    setPageTransformer { page, position ->
        page.alpha = when {
            position <= -1.0F || position >= 1.0F -> 0.0F
            position == 0.0F -> 1.0F
            else -> 1.0F - abs(position)
        }
    }
}

fun String.capitalizeFirstChar() =
    replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() }

fun String.capitalizeWords(): String = split(" ").joinToString(" ") { it.capitalizeFirstChar() }

fun LocalDate.getDayStringFormatted() = format(DateTimeFormatter.ofPattern("EEE ")).replace(".","").capitalizeFirstChar()

fun Fragment.launchWebBrowserIntent(uri: String, packageName: String? = null){
    try {
        Intent(Intent.ACTION_VIEW).let {
            it.data = Uri.parse(uri)
            if (packageName != null) it.setPackage(packageName)
            startActivity(it)
        }

    }catch (e:Exception){
        if (this is BottomSheetDialogFragment) showSnackBar(R.string.snack_general_error)
        else UiInterface.showSnackBar(getString(R.string.snack_general_error))
    }
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
        } catch (e:Exception){
            Log.d("Async block in Broadcast Receiver", e.message.toString())
        }finally {
            // Always call finish(), even if the coroutineScope was cancelled
            result?.finish()
        }
    }
}

fun BottomSheetDialogFragment.showSnackBar(
    @StringRes message: Int,
    duration: Int = Snackbar.LENGTH_LONG,
    anchorView: View? = null,
    actionText: Int = R.string.action_undo,
    actionListener: View.OnClickListener? = null
){
    Snackbar.make(dialog?.window!!.decorView, message, duration).let {
        if (anchorView != null) it.anchorView = anchorView
        if (actionListener != null) it.setAction(actionText, actionListener)
        it.show()
    }
}

fun Context.hideKeyboard(view: View) {
    val inputMethodManager = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
}

fun Fragment.hideKeyboard() {
    view?.let { requireActivity().hideKeyboard(it) }
}

fun LottieAnimationView.playAnimationOnce(@RawRes animation: Int){
    setAnimation(animation)
    repeatCount = 0
    playAnimation()
}

inline val Fragment.UiInterface: UiInterfaceListener
    get() = (requireActivity() as UiInterfaceListener)

fun Int.toMillis() = (this * 1000).toLong()

fun Int.getLevel() = (this / 100) + 1

fun Int.expForNextLevel() = 100 - this % 100

fun Int.expForCompleteLevel() = this % 100