package com.puntogris.posture.ui.main

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationManagerCompat
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import com.google.android.material.snackbar.Snackbar
import com.puntogris.posture.R
import com.puntogris.posture.databinding.ActivityMainBinding
import com.puntogris.posture.utils.constants.Constants.CLAIM_NOTIFICATION_EXP_INTENT
import com.puntogris.posture.utils.constants.Constants.NAVIGATION_DATA
import com.puntogris.posture.utils.constants.Constants.NOTIFICATION_ID
import com.puntogris.posture.utils.constants.Constants.URI_STRING
import com.puntogris.posture.utils.constants.Constants.WEBSITE_HTTPS
import com.puntogris.posture.utils.extensions.equalsAny
import com.puntogris.posture.utils.extensions.getNavController
import com.puntogris.posture.utils.extensions.getNavHostFragment
import com.puntogris.posture.utils.extensions.launchWebBrowserIntent
import com.puntogris.posture.utils.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

@AndroidEntryPoint
class MainActivity :
    AppCompatActivity(),
    NavController.OnDestinationChangedListener,
    UiInterfaceListener {

    private val binding by viewBinding(ActivityMainBinding::inflate)
    private val viewModel: MainViewModel by viewModels()
    private lateinit var navController: NavController
    private lateinit var appBarConfiguration: AppBarConfiguration

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.Theme_Posture)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        setupNavigation()
        checkAppCurrentVersion()
        checkIntentForNavigation(intent)
    }

    private fun checkAppCurrentVersion() {
        lifecycleScope.launch(Dispatchers.Main.immediate) {
            viewModel.appVersionStatus.collectLatest { isNewVersion ->
                if (isNewVersion) navController.navigate(R.id.whatsNewDialog)
            }
        }
    }

    private fun setupNavigation() {
        navController = getNavController()
        appBarConfiguration = getAppBarConfiguration()
        navController.addOnDestinationChangedListener(this)
        setupInitialDestination()
        setupTopToolbar()
        setupBottomNavigation()
    }

    private fun setupInitialDestination() {
        val startDestination = runBlocking {
            if (viewModel.showLogin()) R.id.loginFragment else R.id.homeFragment
        }
        navController.graph = navController.navInflater.inflate(R.navigation.navigation).apply {
            setStartDestination(startDestination)
        }
    }

    private fun setupTopToolbar() {
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        setupActionBarWithNavController(navController, appBarConfiguration)
    }

    private fun setupBottomNavigation() {
        binding.bottomNavigation.apply {
            setOnItemSelectedListener {
                navController.navigate(it.itemId)
                true
            }
            // Trick to disable reloading the same destination if we are there already
            setOnItemReselectedListener {}
        }
    }

    private fun getAppBarConfiguration(): AppBarConfiguration {
        return AppBarConfiguration(
            setOf(
                R.id.homeFragment,
                R.id.accountFragment,
                R.id.newReminderBottomSheet,
                R.id.exercisesFragment,
                R.id.loginFragment,
                R.id.syncAccountFragment,
                R.id.permissionsFragment,
                R.id.batteryOptimizationFragment,
                R.id.rankingsFragment
            )
        )
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
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
                navController.navigate(R.id.settingsFragment)
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    override fun onDestinationChanged(
        controller: NavController,
        destination: NavDestination,
        arguments: Bundle?
    ) {
        binding.bottomNavigation.isVisible = destination.id.equalsAny(
            R.id.homeFragment,
            R.id.exercisesFragment,
            R.id.rankingsFragment,
            R.id.accountFragment
        )
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        checkIntentForNavigation(intent)
    }

    private fun checkIntentForNavigation(intent: Intent?) {
        intent?.extras?.let {
            it.getString(URI_STRING)?.let { uri ->
                if (uri.contains(WEBSITE_HTTPS)) launchWebBrowserIntent(uri)
            }
            it.getString(NAVIGATION_DATA)?.let { intent ->
                if (intent == CLAIM_NOTIFICATION_EXP_INTENT) {
                    navController.navigate(R.id.claimNotificationExpDialog)
                }
            }
            it.getInt(NOTIFICATION_ID).let { id ->
                NotificationManagerCompat.from(this).cancel(id)
            }
        }
    }

    override fun showSnackBar(
        message: String,
        duration: Int,
        actionText: Int,
        anchorToBottomNav: Boolean,
        action: View.OnClickListener?
    ) {
        Snackbar.make(binding.root, message, duration).let {
            if (anchorToBottomNav) it.anchorView = binding.bottomNavigation
            if (action != null) it.setAction(actionText, action)
            it.show()
        }
    }

    override fun onBackPressed() {
        if (isTaskRoot &&
            getNavHostFragment().childFragmentManager.backStackEntryCount == 0 &&
            supportFragmentManager.backStackEntryCount == 0
        ) {
            finishAfterTransition()
        } else {
            super.onBackPressed()
        }
    }
}
