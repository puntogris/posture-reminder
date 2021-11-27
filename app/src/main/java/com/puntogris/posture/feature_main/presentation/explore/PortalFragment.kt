package com.puntogris.posture.feature_main.presentation.explore

import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.puntogris.posture.R
import com.puntogris.posture.databinding.FragmentPortalBinding
import com.puntogris.posture.feature_main.domain.model.Exercise
import com.puntogris.posture.feature_main.domain.model.UserPublicProfile
import com.puntogris.posture.common.presentation.base.BaseFragmentOptions
import com.puntogris.posture.feature_main.presentation.rankings.RankingsAdapter
import com.puntogris.posture.common.utils.*
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class PortalFragment : BaseFragmentOptions<FragmentPortalBinding>(R.layout.fragment_portal) {

    private val viewModel: PortalViewModel by viewModels()
    private lateinit var rankingsAdapter: RankingsAdapter

    override fun initializeViews() {
        binding.fragment = this
        setupRankingsRvAdapter()
        fetchRankingsAndFillAdapter()
        setupExercisesRvAdapter()
    }

    private fun setupExercisesRvAdapter() {
        binding.exercisesRv.adapter = ExercisesAdapter { onExerciseClicked(it) }
    }

    private fun setupRankingsRvAdapter() {
        rankingsAdapter = RankingsAdapter()
        binding.rankingsRv.adapter = rankingsAdapter
    }

    private fun fetchRankingsAndFillAdapter() {
        launchAndRepeatWithViewLifecycle {
            viewModel.rankings.collect {
                handleRankingsResult(it)
            }
        }
    }

    private fun handleRankingsResult(result: Result<List<UserPublicProfile>>) {
        when (result) {
            is Result.Error -> {
                UiInterface.showSnackBar(getString(R.string.snack_connection_error))
            }
            is Result.Success -> {
                binding.portalRankingsShimmer.apply {
                    hideShimmer()
                    stopShimmer()
                    gone()
                }
                rankingsAdapter.submitList(result.value)
            }
        }
    }

    private fun onExerciseClicked(exercise: Exercise) {
        val action = PortalFragmentDirections.actionPortalToExercise(exercise)
        findNavController().navigate(action)
    }

    fun navigateToGlobalRanking() {
        navigateTo(R.id.action_portal_to_rankings)
    }
}