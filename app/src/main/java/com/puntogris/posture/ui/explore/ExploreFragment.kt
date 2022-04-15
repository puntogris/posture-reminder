package com.puntogris.posture.ui.explore

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.puntogris.posture.R
import com.puntogris.posture.databinding.FragmentExploreBinding
import com.puntogris.posture.domain.model.Exercise
import com.puntogris.posture.domain.model.UserPublicProfile
import com.puntogris.posture.ui.rankings.RankingsAdapter
import com.puntogris.posture.utils.Result
import com.puntogris.posture.utils.extensions.*
import com.zhuinden.fragmentviewbindingdelegatekt.viewBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ExploreFragment : Fragment(R.layout.fragment_explore) {

    private val viewModel: PortalViewModel by viewModels()
    private val binding by viewBinding(FragmentExploreBinding::bind)
    private lateinit var rankingsAdapter: RankingsAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRankingsRvAdapter()
        fetchRankingsAndFillAdapter()
        setupExercisesRvAdapter()

        binding.globalRankingsButton.setOnClickListener {
            navigateTo(R.id.action_explore_to_rankings)
        }
    }

    private fun setupExercisesRvAdapter() {
        binding.exercisesRv.adapter = ExercisesAdapter(::onExerciseClicked)
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
        val action = ExploreFragmentDirections.actionExploreToExercise(exercise)
        findNavController().navigate(action)
    }

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