package com.puntogris.posture.ui.settings

import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.puntogris.posture.R
import com.puntogris.posture.databinding.BottomSheetSettingsBinding
import com.puntogris.posture.model.SettingItem
import com.puntogris.posture.model.SettingUi
import com.puntogris.posture.model.SimpleResult
import com.puntogris.posture.ui.base.BaseBottomSheetFragment
import com.puntogris.posture.utils.*
import com.puntogris.posture.utils.Constants.DATA_KEY
import com.puntogris.posture.utils.Constants.EDIT_NAME_KEY
import com.puntogris.posture.utils.Constants.SEND_TICKET_KEY
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SettingsBottomSheet : BaseBottomSheetFragment<BottomSheetSettingsBinding>(R.layout.bottom_sheet_settings, true) {

    private val viewModel: SettingsViewModel by viewModels()
    private lateinit var settingsAdapter: SettingsAdapter

    override fun initializeViews() {
        binding.bottomSheet = this
        setupRecyclerViewAdapter()
        setFragmentResultsListener()
    }

    private fun setupRecyclerViewAdapter(){
        settingsAdapter = SettingsAdapter(requireContext()){ onSettingClicked(it) }
        binding.recyclerView.adapter = settingsAdapter
        setBatteryOptimizationSummary()
        setThemeName()
        setUserName()
    }

    private fun setUserName(){
        lifecycleScope.launch {
            viewModel.getUserFlow().collect {
                settingsAdapter.updateUserName(it.username)
            }
        }
    }

    private fun setBatteryOptimizationSummary(){
        settingsAdapter.updateBatteryOptimization(isIgnoringBatteryOptimizations())
    }

    private fun setThemeName(){
        val themeNames = resources.getStringArray(R.array.theme_names)
        lifecycleScope.launch {
            viewModel.getThemeNamePosition().collect {
                settingsAdapter.updateThemeName(themeNames[it])
            }
        }
    }

    private fun onSettingClicked(settingItem: SettingItem){
        when(settingItem.code){
            SettingUi.BatteryOpt -> onBatteryClicked()
            SettingUi.Github -> onGithubClicked()
            SettingUi.Help -> onHelpClicked()
            SettingUi.Name -> onNameClicked()
            SettingUi.RateApp -> onRateAppClicked()
            SettingUi.Theme -> onThemeClicked()
            SettingUi.Ticket -> onTicketClicked()
            SettingUi.Version -> onVersionClicked()
            SettingUi.Website -> onWebsiteClicked()
            SettingUi.LogOut -> onLogOutClicked()
        }
    }

    private fun onBatteryClicked(){
        findNavController().navigate(R.id.batteryOptimizationFragment)
    }

    private fun onNameClicked(){
        lifecycleScope.launch {
            val name = viewModel.getUserFlow().first().username
            val action = SettingsBottomSheetDirections.actionSettingsBottomSheetToDialogName(name)
            findNavController().navigate(action)
        }
    }
    private fun onRateAppClicked(){
        launchWebBrowserIntent(
            "https://play.google.com/store/apps/details?id=com.puntogris.posture",
            "com.puntogris.posture")
    }
    private fun onThemeClicked(){
        findNavController().navigate(R.id.selectThemeDialog)
    }
    private fun onTicketClicked(){
        findNavController().navigate(R.id.action_settingsBottomSheet_to_ticketBottomSheet)

    }
    private fun onVersionClicked(){
        viewModel.setPandaAnimationPref(true)
        showSnackBar(R.string.snack_panda_unlocked)
    }
    private fun onWebsiteClicked(){
        launchWebBrowserIntent("https://www.postureapp.puntogris.com")
    }
    private fun onGithubClicked(){
        launchWebBrowserIntent("https://www.github.com/puntogris")
    }
    private fun onHelpClicked(){
        launchWebBrowserIntent("https://www.postureapp.puntogris.com/help")
    }
    private fun onLogOutClicked(){
        when(viewModel.logOut()){
            SimpleResult.Failure -> showSnackBar(R.string.snack_general_error)
            SimpleResult.Success -> findNavController().navigate(R.id.loginFragment)
        }
    }

    private fun setFragmentResultsListener(){
        setFragmentResultListener(SEND_TICKET_KEY){ _, bundle ->
            val ticketSentSuccessfully = bundle.getBoolean(DATA_KEY)
            val snackMessage =
                if (ticketSentSuccessfully) R.string.snack_send_ticket_success
                else R.string.snack_connection_error
            showSnackBar(snackMessage)
        }
        setFragmentResultListener(EDIT_NAME_KEY){_, bundle ->
            val editUsernameSuccessfully = bundle.getBoolean(DATA_KEY)
            val snackMessage =
                if (editUsernameSuccessfully) R.string.snack_edit_username_success
                else R.string.snack_connection_error
            showSnackBar(snackMessage)
        }
    }
}

