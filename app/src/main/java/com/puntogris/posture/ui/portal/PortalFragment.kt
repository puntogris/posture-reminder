package com.puntogris.posture.ui.portal

import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.puntogris.posture.R
import com.puntogris.posture.databinding.FragmentPortalBinding
import com.puntogris.posture.model.Exercise
import com.puntogris.posture.model.RepoResult
import com.puntogris.posture.model.UserPublicProfile
import com.puntogris.posture.ui.base.BaseFragmentOptions
import com.puntogris.posture.ui.rankings.RankingsAdapter
import com.puntogris.posture.utils.gone
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

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

    private fun setupExercisesRvAdapter(){
        binding.exercisesRv.adapter = ExercisesAdapter{ onExerciseClicked(it) }
    }

    private fun setupRankingsRvAdapter(){
        rankingsAdapter = RankingsAdapter()
        binding.rankingsRv.adapter = rankingsAdapter
    }

    private fun fetchRankingsAndFillAdapter(){
        lifecycleScope.launch {
            val result = viewModel.getTopThreeRankings()
            handleResultFromFetchRankings(result)
        }
    }

    private fun handleResultFromFetchRankings(result: RepoResult<List<UserPublicProfile>>){
        when(result){
            is RepoResult.Error -> {
                //show snack or change ui to show error
            }
            is RepoResult.Success -> {
                binding.shimmerViewContainer.apply {
                    hideShimmer()
                    stopShimmer()
                    gone()
                }
                rankingsAdapter.submitList(result.data)
            }
        }
    }

    private fun onExerciseClicked(exercise: Exercise){
        val action = PortalFragmentDirections.actionPortalFragmentToExerciseBottomSheet(exercise)
        findNavController().navigate(action)
    }

    fun navigateToGlobalRanking(){
        findNavController().navigate(R.id.action_portalFragment_to_rankingsFragment)
    }

}