package com.puntogris.posture.ui.main

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatDelegate
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.ui.*
import com.google.android.material.snackbar.Snackbar
import com.puntogris.posture.R
import com.puntogris.posture.databinding.ActivityMainBinding
import com.puntogris.posture.ui.base.BaseActivity
import com.puntogris.posture.utils.*
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : BaseActivity<ActivityMainBinding>(R.layout.activity_main) {

    private lateinit var navController: NavController
    private lateinit var appBarConfiguration: AppBarConfiguration
    private val viewModel: MainViewModel by viewModels()

    @Inject lateinit var dataStore: DataStore

    override fun preInitViews() {
        applyAppTheme()
    }

    private fun applyAppTheme(){
        setTheme(R.style.Theme_Posture)
        val theme = runBlocking { dataStore.appTheme() }
        AppCompatDelegate.setDefaultNightMode(theme)
    }

    override fun initializeViews() {
        setupNavigation()
        checkAppCurrentVersion()
    }

    private fun checkAppCurrentVersion(){
        viewModel.appVersionStatus.observe(this){ isNewVersion ->
            if (isNewVersion) navController.navigate(R.id.whatsNewDialog)
        }
    }

    private fun setupNavigation() {
        navController = getNavController()
        appBarConfiguration = getAppBarConfiguration()
        navController.addOnDestinationChangedListener(this@MainActivity)
        //call after navController is set
        setupInitialDestination()
        setupTopToolbar()
        setupBottomNavigation()
    }

    private fun setupInitialDestination(){
        navController.graph = navController.navInflater.inflate(R.navigation.navigation)
            .apply {
                startDestination =
                    if (viewModel.isUserLoggedIn()) R.id.mainFragment
                    else R.id.loginFragment
            }
    }

    private fun setupTopToolbar(){
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        setupActionBarWithNavController(navController, appBarConfiguration)
    }

    private fun setupBottomNavigation(){
        binding.bottomNavigation.apply {
            setupWithNavController(navController)
            //trick to disable reloading the same destination if we are there already
            setOnItemReselectedListener {}
        }
    }

    private fun getAppBarConfiguration(): AppBarConfiguration{
        return AppBarConfiguration(
            setOf(
                R.id.mainFragment,
                R.id.accountFragment,
                R.id.newReminderBottomSheet,
                R.id.welcomeFragment,
                R.id.portalFragment,
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
        if (destination.id == R.id.welcomeFragment ||
            destination.id == R.id.synAccountFragment ||
            destination.id == R.id.batteryOptimizationFragment ||
            destination.id == R.id.loginFragment
        ) {
            binding.bottomNavigation.gone()
        } else {
            binding.bottomNavigation.visible()
        }
    }

    override fun showSnackBar(message: String,
                              duration: Int,
                              actionText: Int,
                              actionListener: View.OnClickListener?){
        Snackbar.make(binding.root, message, duration).let {
            it.anchorView = binding.bottomNavigation
            if (actionListener != null) it.setAction(actionText, actionListener)
            it.show()
        }
    }
}
