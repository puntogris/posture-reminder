package com.puntogris.posture.ui.rankings

import androidx.fragment.app.viewModels
import com.puntogris.posture.R
import com.puntogris.posture.databinding.FragmentRankingsBinding
import com.puntogris.posture.model.RepoResult
import com.puntogris.posture.model.UserPublicProfile
import com.puntogris.posture.ui.base.BaseFragmentOptions
import com.puntogris.posture.utils.UiInterface
import com.puntogris.posture.utils.launchAndRepeatWithViewLifecycle
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RankingsFragment : BaseFragmentOptions<FragmentRankingsBinding>(R.layout.fragment_rankings) {

    private val viewModel: RankingsViewModel by viewModels()
    private lateinit var rankingsAdapter: RankingsAdapter

    override fun initializeViews() {
        setupRecyclerViewAdapter()
        fetchRankingsAndFillAdapter()
    }

    private fun setupRecyclerViewAdapter() {
        rankingsAdapter = RankingsAdapter()
        binding.recyclerView.adapter = rankingsAdapter
    }

    private fun fetchRankingsAndFillAdapter() {
        launchAndRepeatWithViewLifecycle {
            val result = viewModel.getAllRankings()
            handleResultFromFetchRankings(result)
        }
    }

    private fun handleResultFromFetchRankings(result: RepoResult<List<UserPublicProfile>>) {
        when (result) {
            is RepoResult.Error -> UiInterface.showSnackBar(getString(R.string.snack_connection_error))
            is RepoResult.Success -> rankingsAdapter.submitList(result.data)
        }
    }
}
