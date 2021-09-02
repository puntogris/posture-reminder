package com.puntogris.posture.utils

import android.animation.ObjectAnimator
import android.app.Activity
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.net.Uri
import android.os.PowerManager
import android.view.Menu
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.annotation.RawRes
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.NavHostFragment
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import androidx.viewpager2.widget.ViewPager2
import com.airbnb.lottie.LottieAnimationView
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.snackbar.Snackbar
import com.puntogris.posture.R
import com.puntogris.posture.ui.main.MainActivity
import com.puntogris.posture.ui.main.UiInterfaceListener
import com.puntogris.posture.utils.Constants.EXPERIENCE_PER_LEVEL
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.threeten.bp.LocalDate
import org.threeten.bp.format.DateTimeFormatter
import timber.log.Timber
import java.util.*
import kotlin.math.abs

fun View.gone() {
    visibility = View.GONE
}

fun View.visible() {
    visibility = View.VISIBLE
}

fun View.playShakeAnimation(){
    ObjectAnimator
        .ofFloat(this,"translationX", 0f, 25f, -25f, 25f, -25f,15f, -15f, 6f, -6f, 0f)
        .setDuration(Constants.SHAKE_ANIMATION_DURATION)
        .start()
}

fun Int.getHours() = this / 60

fun Int.getMinutes() = this % 60

fun Long.millisToMinutes() = (this / 1000 / 60).toInt()

fun Int.minutesToMillis() = this * 1000 * 60

fun Int.toMillis() = (this * 1000).toLong()

fun Int.getLevel() = (this / EXPERIENCE_PER_LEVEL) + 1

fun Int.expForNextLevel() = EXPERIENCE_PER_LEVEL - this % EXPERIENCE_PER_LEVEL

fun Int.expForCompleteLevel() = this % EXPERIENCE_PER_LEVEL

fun Long.toDays() = (this / (1000 * 60 * 60 * 24)).toInt()

fun String.capitalizeFirstChar() =
    replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() }

fun String.capitalizeWords(): String = split(" ").joinToString(" ") { it.capitalizeFirstChar() }

fun AppCompatActivity.getNavController() = getNavHostFragment().navController

fun AppCompatActivity.getNavHostFragment() =
    (supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment)

fun Context.hideKeyboard(view: View) {
    val inputMethodManager = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
}

fun Fragment.hideKeyboard() {
    view?.let { requireActivity().hideKeyboard(it) }
}

inline val Fragment.UiInterface: UiInterfaceListener
    get() = (requireActivity() as UiInterfaceListener)

fun Fragment.isDarkThemeOn() =
    (resources.configuration.uiMode and
            Configuration.UI_MODE_NIGHT_MASK == Configuration.UI_MODE_NIGHT_YES)

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

fun Activity.launchWebBrowserIntent(uri: String){
    try {
        Intent(Intent.ACTION_VIEW).let {
            it.data = Uri.parse(uri)
            startActivity(it)
        }
    }catch (e:Exception){
        (this as MainActivity).showSnackBar(getString(R.string.snack_general_error))
    }
}

fun Fragment.isIgnoringBatteryOptimizations(): Boolean{
    val pm = requireContext().getSystemService(PowerManager::class.java)
    return (pm.isIgnoringBatteryOptimizations(requireActivity().packageName))
}

inline fun Fragment.launchAndRepeatWithViewLifecycle(
    minActiveState: Lifecycle.State = Lifecycle.State.STARTED,
    crossinline block: suspend CoroutineScope.() -> Unit
) {
    viewLifecycleOwner.lifecycleScope.launch {
        viewLifecycleOwner.lifecycle.repeatOnLifecycle(minActiveState) {
            block()
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

inline fun <T> MutableLiveData<T>.setField(transform: T.() -> Unit) { this.value = this.value?.apply(transform) }

fun ViewPager2.setPageFadeTransformer(){
    setPageTransformer { page, position ->
        page.alpha = when {
            position <= -1.0F || position >= 1.0F -> 0.0F
            position == 0.0F -> 1.0F
            else -> 1.0F - abs(position)
        }
    }
}

fun LocalDate.getDayStringFormatted() = format(DateTimeFormatter.ofPattern("EEE ")).replace(".","").capitalizeFirstChar()

fun Menu.showItem(item: Int){
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
        } catch (e:Exception) {
            Timber.d(e.message.toString())
        }finally {
            result?.finish()
        }
    }
}

fun LottieAnimationView.playAnimationOnce(@RawRes animation: Int){
    setAnimation(animation)
    repeatCount = 0
    playAnimation()
}

inline fun PreferenceFragmentCompat.preference(key: String, block: Preference.() -> Unit){
    findPreference<Preference>(key)?.apply {
        block(this)
    }
}

inline fun Preference.onClick(crossinline block: () -> Unit){
    setOnPreferenceClickListener {
        block()
        true
    }
}

inline fun PreferenceFragmentCompat.preferenceOnClick(key: String, crossinline block: () -> Unit){
    findPreference<Preference>(key)?.setOnPreferenceClickListener {
        block()
        true
    }
}

inline val EditText.data
    get() = text.toString()

inline val Date.timeWithZoneOffset
    get() = time + TimeZone.getDefault().getOffset(time)
