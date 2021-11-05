package com.puntogris.posture.ui.portal

import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.puntogris.posture.R
import com.puntogris.posture.databinding.FragmentPortalBinding
import com.puntogris.posture.model.Exercise
import com.puntogris.posture.model.UserPublicProfile
import com.puntogris.posture.ui.base.BaseFragmentOptions
import com.puntogris.posture.ui.rankings.RankingsAdapter
import com.puntogris.posture.utils.UiInterface
import com.puntogris.posture.utils.gone
import com.puntogris.posture.utils.launchAndRepeatWithViewLifecycle
import com.puntogris.posture.utils.navigateTo
import dagger.hilt.android.AndroidEntryPoint
import com.puntogris.posture.utils.Result

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
            val result = viewModel.getTopThreeRankings()
            handleResultFromFetchRankings(result)
        }
    }

    private fun handleResultFromFetchRankings(result: Result<Exception, List<UserPublicProfile>>) {
        when (result) {
            is Result.Error -> {
                UiInterface.showSnackBar(getString(R.string.snack_connection_error))
            }
            is Result.Success -> {
                binding.shimmerViewContainer.apply {
                    hideShimmer()
                    stopShimmer()
                    gone()
                }
                rankingsAdapter.submitList(result.value)
            }
        }
    }

    private fun onExerciseClicked(exercise: Exercise) {
        val action = PortalFragmentDirections.actionPortalFragmentToExerciseBottomSheet(exercise)
        findNavController().navigate(action)
    }

    fun navigateToGlobalRanking() {
        navigateTo(R.id.action_portalFragment_to_rankingsFragment)
    }

}