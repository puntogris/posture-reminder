package com.puntogris.posture.common.presentation.base

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import androidx.annotation.LayoutRes
import androidx.databinding.ViewDataBinding
import com.puntogris.posture.R
import com.puntogris.posture.common.utils.showItem

abstract class BaseFragmentOptions<T : ViewDataBinding>(@LayoutRes override val layout: Int) :
    BaseBindingFragment<T>(layout) {

    override fun onCreate(savedInstanceState: Bundle?) {
        setHasOptionsMenu(true)
        super.onCreate(savedInstanceState)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        menu.showItem(R.id.settings)
        menu.showItem(R.id.newReminder)
        super.onCreateOptionsMenu(menu, inflater)
    }
}