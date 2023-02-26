package com.puntogris.posture.utils.extensions

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import com.puntogris.posture.R
import com.puntogris.posture.ui.main.MainActivity

fun AppCompatActivity.getNavHostFragment() =
    (supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment)

fun AppCompatActivity.getNavController() = getNavHostFragment().navController

fun Activity.launchWebBrowserIntent(uri: String) {
    try {
        Intent(Intent.ACTION_VIEW).let {
            it.data = Uri.parse(uri)
            startActivity(it)
        }
    } catch (e: Exception) {
        (this as MainActivity).showSnackBar(getString(R.string.snack_general_error))
    }
}
