package com.puntogris.posture.ui

import android.os.Bundle
import android.view.*
import androidx.databinding.DataBindingUtil.inflate
import androidx.fragment.app.Fragment
import com.puntogris.posture.R
import com.puntogris.posture.databinding.FragmentMainBinding
import com.puntogris.posture.preferences.SharedPref
import com.puntogris.posture.utils.Utils
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainFragment : Fragment() {
    private lateinit var binding: FragmentMainBinding
    @Inject lateinit var sharedPref: SharedPref

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        binding = inflate(inflater, R.layout.fragment_main, container, false)
        binding.mainFragment = this
        checkAppStatus()
        Utils.createNotificationChannel(requireActivity())


        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        setHasOptionsMenu(true)
        super.onCreate(savedInstanceState)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        menu.findItem(R.id.preferencesFragment).isVisible = true
        super.onCreateOptionsMenu(menu, inflater)
    }

    private fun checkAppStatus(){
        binding.enableTextView.text = sharedPref.appStatusText()
        binding.enableSummaryTextview.text = sharedPref.appStatusSummaryText()
    }

    fun changeAppStatusPref(){
        binding.enableTextView.text = sharedPref.changeAppStatus()
        binding.enableSummaryTextview.text = sharedPref.appStatusSummaryText()
    }

}