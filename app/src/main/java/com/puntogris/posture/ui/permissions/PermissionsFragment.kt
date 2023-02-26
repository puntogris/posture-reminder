package com.puntogris.posture.ui.permissions

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.view.View
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.puntogris.posture.R
import com.puntogris.posture.databinding.FragmentPermissionsBinding
import com.puntogris.posture.utils.PermissionsManager.needsAlarmPermission
import com.puntogris.posture.utils.PermissionsManager.needsNotificationPermission
import com.puntogris.posture.utils.constants.Constants
import com.puntogris.posture.utils.extensions.showSnackBar
import com.puntogris.posture.utils.viewBinding

class PermissionsFragment : Fragment(R.layout.fragment_permissions) {

    private val binding: FragmentPermissionsBinding by viewBinding(FragmentPermissionsBinding::bind)
    private lateinit var intentPermissionLauncher: ActivityResultLauncher<Intent>
    private lateinit var stringPermissionLauncher: ActivityResultLauncher<String>

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupPermissionLaunchers()
        setupListeners()
    }

    private fun setupPermissionLaunchers() {
        stringPermissionLauncher = registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) {
            onPermissionResult(it)
        }
        intentPermissionLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) {
            onPermissionResult(it.resultCode == Activity.RESULT_OK)
        }
    }

    private fun setupListeners() {
        binding.buttonSkip.setOnClickListener {
            findNavController().navigate(R.id.homeFragment)
        }
        binding.buttonContinue.setOnClickListener {
            continuePermissionsFlow()
        }
    }

    private fun onPermissionResult(isGranted: Boolean) {
        if (isGranted) {
            continuePermissionsFlow()
        } else {
            showSnackBar(R.string.snack_permission_required, binding.buttonContinue)
        }
    }

    private fun continuePermissionsFlow() {
        when {
            needsAlarmPermission(requireContext()) -> showAlarmsSettings()
            needsNotificationPermission(requireContext()) -> onNeedsNotificationPermission()
            else -> findNavController().navigate(R.id.homeFragment)
        }
    }

    private fun onNeedsNotificationPermission() {
        val canAskNotificationPermission = Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU &&
                shouldShowRequestPermissionRationale(Manifest.permission.POST_NOTIFICATIONS)
        if (canAskNotificationPermission) {
            stringPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val intent = Intent().apply {
                action = Settings.ACTION_APP_NOTIFICATION_SETTINGS
                putExtra(Settings.EXTRA_APP_PACKAGE, requireContext().packageName)
            }
            intentPermissionLauncher.launch(intent)
        } else {
            val intent = Intent().apply {
                action = "android.settings.APP_NOTIFICATION_SETTINGS"
                putExtra("app_package", requireContext().packageName)
                putExtra("app_uid", requireContext().applicationInfo.uid)
            }
            intentPermissionLauncher.launch(intent)
        }
    }

    @SuppressLint("InlinedApi")
    // No need to check the API level as we validate that when checking if the permission is granted
    private fun showAlarmsSettings() {
        val intent = Intent(Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM).apply {
            data = Uri.parse(Constants.PACKAGE_URI_NAME)
        }
        intentPermissionLauncher.launch(intent)
    }
}
