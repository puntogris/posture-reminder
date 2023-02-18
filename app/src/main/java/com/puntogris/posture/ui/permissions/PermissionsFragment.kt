package com.puntogris.posture.ui.permissions

import android.Manifest
import android.app.Activity
import android.app.AlarmManager
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.view.View
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.NotificationManagerCompat
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import androidx.navigation.fragment.findNavController
import com.puntogris.posture.R
import com.puntogris.posture.databinding.FragmentPermissionsBinding
import com.puntogris.posture.utils.constants.Constants
import com.puntogris.posture.utils.constants.Constants.DATA_KEY
import com.puntogris.posture.utils.constants.Constants.PERMISSION_KEY
import com.puntogris.posture.utils.extensions.showSnackBar
import com.puntogris.posture.utils.viewBinding

class PermissionsFragment : Fragment(R.layout.fragment_permissions) {

    private val binding: FragmentPermissionsBinding by viewBinding(FragmentPermissionsBinding::bind)
    private lateinit var intentPermissionLauncher: ActivityResultLauncher<Intent>
    private lateinit var stringPermissionLauncher: ActivityResultLauncher<String>

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

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
        setupListeners()
    }

    private fun setupListeners() {
        binding.buttonSkip.setOnClickListener {
            setResultAndNavigateUp(false)
        }
        binding.buttonContinue.setOnClickListener {
            continuePermissionsFlow()
        }
    }

    private fun onPermissionResult(isGranted: Boolean) {
        if (isGranted) {
            continuePermissionsFlow()
        } else {
            showSnackBar(R.string.snack_permission_required)
        }
    }

    private fun setResultAndNavigateUp(isGranted: Boolean) {
        setFragmentResult(PERMISSION_KEY, bundleOf(DATA_KEY to isGranted))
        findNavController().navigateUp()
    }

    private fun continuePermissionsFlow() {
        val alarmManager = requireContext().getSystemService(AlarmManager::class.java)
        val notificationManager = NotificationManagerCompat.from(requireContext())
        when {
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.S && !alarmManager.canScheduleExactAlarms() -> {
                intentPermissionLauncher.launch(
                    Intent(Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM).apply {
                        data = Uri.parse(Constants.PACKAGE_URI_NAME)
                    }
                )
            }
            !notificationManager.areNotificationsEnabled() -> {
                stringPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
            else -> setResultAndNavigateUp(true)
        }
    }
}
