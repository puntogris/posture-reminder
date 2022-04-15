package com.puntogris.posture.utils.extensions

import android.content.Intent
import android.content.res.Configuration
import android.net.Uri
import androidx.annotation.IdRes
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.puntogris.posture.R
import com.puntogris.posture.ui.main.UiInterfaceListener
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

fun Fragment.navigateTo(@IdRes id: Int) = findNavController().navigate(id)

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

fun Fragment.isDarkThemeOn() = requireContext().isDarkThemeOn()

fun Fragment.launchWebBrowserIntent(uri: String, packageName: String? = null) {
    try {
        Intent(Intent.ACTION_VIEW).let {
            it.data = Uri.parse(uri)
            if (packageName != null) it.setPackage(packageName)
            startActivity(it)
        }

    } catch (e: Exception) {
        if (this is BottomSheetDialogFragment) showSnackBar(R.string.snack_general_error)
        else UiInterface.showSnackBar(getString(R.string.snack_general_error))
    }
}

inline val Fragment.UiInterface: UiInterfaceListener
    get() = (requireActivity() as UiInterfaceListener)

