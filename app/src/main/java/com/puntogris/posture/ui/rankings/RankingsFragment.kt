package com.puntogris.posture.ui.rankings

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.puntogris.posture.R
import com.puntogris.posture.databinding.FragmentRankingsBinding
import com.puntogris.posture.utils.Result
import com.puntogris.posture.utils.extensions.UiInterface
import com.puntogris.posture.utils.extensions.launchAndRepeatWithViewLifecycle
import com.puntogris.posture.utils.viewBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RankingsFragment : Fragment(R.layout.fragment_rankings) {

    private val viewModel: RankingsViewModel by viewModels()
    private val binding by viewBinding(FragmentRankingsBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        RankingsAdapter().let {
            binding.recyclerView.adapter = it
            subscribeUi(it)
        }
    }

    private fun subscribeUi(adapter: RankingsAdapter) {
        launchAndRepeatWithViewLifecycle {
            viewModel.rankings.collect {
                when (it) {
                    is Result.Error -> {
                        UiInterface.showSnackBar(getString(R.string.snack_connection_error))
                    }
                    is Result.Success -> {
                        adapter.submitList(it.value)
                    }
                }
            }
        }
    }
}
