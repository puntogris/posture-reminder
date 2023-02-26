package com.puntogris.posture.ui.rankings

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.core.view.MenuProvider
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import com.puntogris.posture.R
import com.puntogris.posture.databinding.FragmentRankingsBinding
import com.puntogris.posture.utils.Result
import com.puntogris.posture.utils.extensions.UiInterface
import com.puntogris.posture.utils.extensions.launchAndRepeatWithViewLifecycle
import com.puntogris.posture.utils.extensions.showItem
import com.puntogris.posture.utils.viewBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RankingsFragment : Fragment(R.layout.fragment_rankings), MenuProvider {

    private val viewModel: RankingsViewModel by viewModels()
    private val binding by viewBinding(FragmentRankingsBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        RankingsAdapter().let {
            binding.recyclerViewRankings.adapter = it
            subscribeUi(it)
        }
        requireActivity().addMenuProvider(this, viewLifecycleOwner, Lifecycle.State.RESUMED)
    }

    private fun subscribeUi(adapter: RankingsAdapter) {
        launchAndRepeatWithViewLifecycle {
            viewModel.rankings.collect {
                when (it) {
                    is Result.Error -> {
                        binding.progressBar.isVisible = false
                        UiInterface.showSnackBar(getString(R.string.snack_connection_error))
                    }
                    is Result.Success -> {
                        adapter.submitList(it.value)
                        binding.progressBar.isVisible = false
                    }
                    is Result.Loading -> {
                        binding.progressBar.isVisible = true
                    }
                }
            }
        }
    }

    override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
        menu.showItem(R.id.settings)
        menu.showItem(R.id.newReminder)
    }

    override fun onMenuItemSelected(menuItem: MenuItem): Boolean = true
}
