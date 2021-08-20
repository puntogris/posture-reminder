package com.puntogris.posture.ui.settings

import android.app.Dialog
import android.os.Bundle
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.lifecycleScope
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.puntogris.posture.R
import com.puntogris.posture.utils.DataStore
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

@AndroidEntryPoint
class SelectThemeDialog: DialogFragment(){

    @Inject
    lateinit var dataStore: DataStore

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val themeValuesArray = resources.getIntArray(R.array.theme_values)

        val currentPosition = runBlocking {
            val currentTheme = dataStore.appTheme()
            if (currentTheme == -1) 2 else currentTheme - 1
        }

        return MaterialAlertDialogBuilder(requireContext(), R.style.MaterialAlertDialog_rounded)
            .setSingleChoiceItems(R.array.theme_names, currentPosition){ _, position ->
                themeValuesArray[position].let {
                    lifecycleScope.launch {
                        dataStore.setAppTheme(it)
                        AppCompatDelegate.setDefaultNightMode(it)
                        dismiss()
                    }
                }
            }
            .create()
    }
}
