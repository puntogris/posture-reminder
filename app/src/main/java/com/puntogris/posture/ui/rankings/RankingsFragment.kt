package com.puntogris.posture.ui.rankings

import androidx.fragment.app.viewModels
import com.puntogris.posture.R
import com.puntogris.posture.databinding.FragmentRankingsBinding
import com.puntogris.posture.ui.base.BaseFragmentOptions
import com.puntogris.posture.utils.Result
import com.puntogris.posture.utils.UiInterface
import com.puntogris.posture.utils.launchAndRepeatWithViewLifecycle
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class RankingsFragment : BaseFragmentOptions<FragmentRankingsBinding>(R.layout.fragment_rankings) {

    private val viewModel: RankingsViewModel by viewModels()

    override fun initializeViews() {
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
