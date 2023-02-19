package com.puntogris.posture.utils.extensions

import android.content.Intent
import android.net.Uri
import android.view.View
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.snackbar.Snackbar
import com.puntogris.posture.R
import com.puntogris.posture.ui.main.UiInterfaceListener
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

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
fun Fragment.launchWebBrowserIntent(uri: Int, packageName: String? = null) {
    launchWebBrowserIntent(getString(uri), packageName)
}

inline val Fragment.UiInterface: UiInterfaceListener
    get() = (requireActivity() as UiInterfaceListener)

inline fun Fragment.setIntentLauncher(crossinline block: (ActivityResult) -> Unit): ActivityResultLauncher<Intent> {
    return registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        block(it)
    }
}

fun Fragment.showSnackBar(
    message: Int,
    anchor: View? = null,
    actionRes: Int? = null,
    action: View.OnClickListener? = null
) {
    Snackbar.make(requireView(), message, Snackbar.LENGTH_LONG).apply {
        anchorView = anchor
        if (actionRes != null && action != null) {
            setAction(actionRes, action)
        }
    }.show()
}

fun Fragment.hideKeyBoard() {
    WindowCompat
        .getInsetsController(requireActivity().window, requireView())
        .hide(WindowInsetsCompat.Type.ime())
}
