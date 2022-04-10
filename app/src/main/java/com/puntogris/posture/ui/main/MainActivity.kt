package com.puntogris.posture.ui.main

import android.content.ContentResolver
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.activity.viewModels
import androidx.core.app.NotificationManagerCompat
import androidx.core.view.isVisible
import androidx.navigation.*
import androidx.navigation.ui.*
import com.google.android.material.snackbar.Snackbar
import com.puntogris.posture.R
import com.puntogris.posture.databinding.ActivityMainBinding
import com.puntogris.posture.ui.base.BaseBindingActivity
import com.puntogris.posture.utils.*
import com.puntogris.posture.utils.constants.Constants.CLAIM_NOTIFICATION_EXP_INTENT
import com.puntogris.posture.utils.constants.Constants.NAVIGATION_DATA
import com.puntogris.posture.utils.constants.Constants.NOTIFICATION_ID
import com.puntogris.posture.utils.constants.Constants.URI_STRING
import com.puntogris.posture.utils.constants.Constants.WEBSITE_HTTPS
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*

@AndroidEntryPoint
class MainActivity : BaseBindingActivity<ActivityMainBinding>(R.layout.activity_main) {

    private lateinit var navController: NavController
    private lateinit var appBarConfiguration: AppBarConfiguration
    private val viewModel: MainViewModel by viewModels()

    override fun preInitializeViews() {
        setTheme(R.style.Theme_Posture)
    }

    override fun initializeViews() {
        setupNavigation()
        checkAppCurrentVersion()
        checkIntentForNavigation(intent)
    }

    private fun checkAppCurrentVersion() {
        viewModel.appVersionStatus.observe(this) { isNewVersion ->
            if (isNewVersion) navController.navigate(R.id.whatsNewDialog)
        }
    }

    private fun setupNavigation() {
        navController = getNavController()
        appBarConfiguration = getAppBarConfiguration()
        navController.addOnDestinationChangedListener(this)

        // Call after navController is set
        setupInitialDestination()
        setupTopToolbar()
        setupBottomNavigation()
    }

    private fun setupInitialDestination() {
        navController.graph = navController.navInflater.inflate(R.navigation.navigation)
            .apply {
                startDestination = runBlocking {
                    if (viewModel.showLogin()) R.id.loginFragment
                    else R.id.homeFragment
                }
            }
    }

    private fun setupTopToolbar() {
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        setupActionBarWithNavController(navController, appBarConfiguration)
    }

    private fun setupBottomNavigation() {
        binding.bottomNavigation.apply {
            setupWithNavController(navController)
            //trick to disable reloading the same destination if we are there already
            setOnItemReselectedListener {}
        }
    }

    private fun getAppBarConfiguration(): AppBarConfiguration {
        return AppBarConfiguration(
            setOf(
                R.id.homeFragment,
                R.id.accountFragment,
                R.id.newReminderBottomSheet,
                R.id.welcomeFragment,
                R.id.exploreFragment,
                R.id.loginFragment,
                R.id.synAccountFragment
            )
        )
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.toolbar, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.newReminder -> {
                navController.navigate(R.id.newReminderBottomSheet)
                true
            }
            R.id.settings -> {
                navController.navigate(R.id.settingsBottomSheet)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onSupportNavigateUp() =
        navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()

    override fun onDestinationChanged(
        controller: NavController,
        destination: NavDestination,
        arguments: Bundle?
    ) {

        binding.bottomNavigation.isVisible = destination.id.notEqualsAny(
            R.id.welcomeFragment,
            R.id.synAccountFragment,
            R.id.batteryOptimizationFragment,
            R.id.loginFragment,
            R.id.whatsNewDialog,
            R.id.internalLoginFragment,
            R.id.deleteAccountFragment
        )
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        checkIntentForNavigation(intent)
    }

    private fun checkIntentForNavigation(intent: Intent?) {
        intent?.extras?.apply {
            getString(URI_STRING)?.let {
                if (it.contains(WEBSITE_HTTPS)) launchWebBrowserIntent(it)
            }
            getString(NAVIGATION_DATA)?.let {
                if (it == CLAIM_NOTIFICATION_EXP_INTENT) {
                    navController.navigate(R.id.claimNotificationExpDialog)
                }
            }
            getInt(NOTIFICATION_ID).let {
                NotificationManagerCompat.from(this@MainActivity).cancel(it)
            }
        }
    }

    override fun showSnackBar(
        message: String,
        duration: Int,
        actionText: Int,
        anchorToBottomNav: Boolean,
        actionListener: View.OnClickListener?
    ) {

        Snackbar.make(binding.root, message, duration).let {
            if (anchorToBottomNav) it.anchorView = binding.bottomNavigation
            if (actionListener != null) it.setAction(actionText, actionListener)
            it.show()
        }
    }
}
