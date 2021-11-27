package com.puntogris.posture.common.presentation.base

import android.os.Bundle
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.navigation.NavController
import com.puntogris.posture.feature_main.presentation.main.UiInterfaceListener
import com.puntogris.posture.common.utils.getNavHostFragment

abstract class BaseBindingActivity<T : ViewDataBinding>(@LayoutRes val layout: Int) :
    AppCompatActivity(),
    NavController.OnDestinationChangedListener,
    UiInterfaceListener {
    private var _binding: T? = null
    val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        preInitializeViews()
        super.onCreate(savedInstanceState)
        _binding = DataBindingUtil.setContentView(this, layout)
        initializeViews()
    }

    open fun initializeViews() {}
    open fun preInitializeViews() {}

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun onBackPressed() {
        if (isTaskRoot &&
            getNavHostFragment().childFragmentManager.backStackEntryCount == 0 &&
            supportFragmentManager.backStackEntryCount == 0
        ) {
            finishAfterTransition()
        } else super.onBackPressed()
    }
}