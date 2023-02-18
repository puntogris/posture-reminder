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
import com.puntogris.posture.utils.extensions.gone
import com.puntogris.posture.utils.extensions.launchAndRepeatWithViewLifecycle
import com.puntogris.posture.utils.extensions.showItem
import com.puntogris.posture.utils.extensions.showSnackBar
import com.puntogris.posture.utils.viewBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ExploreFragment : Fragment(R.layout.fragment_explore) {

    private val viewModel: PortalViewModel by viewModels()
    private val binding by viewBinding(FragmentExploreBinding::bind)
    private lateinit var rankingsAdapter: RankingsAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupObservers()
        setupViews()
    }

    private fun setupViews() {
        rankingsAdapter = RankingsAdapter()
        binding.rankingsRv.adapter = rankingsAdapter
        binding.exercisesRv.adapter = ExercisesAdapter(::onExerciseClicked)
        binding.globalRankingsButton.setOnClickListener {
            findNavController().navigate(R.id.action_explore_to_rankings)
        }
    }

    private fun setupObservers() {
        launchAndRepeatWithViewLifecycle {
            viewModel.rankings.collect {
                handleRankingsResult(it)
            }
        }
    }

    private fun handleRankingsResult(result: Result<List<UserPublicProfile>>) {
        when (result) {
            is Result.Error -> showSnackBar(R.string.snack_connection_error)
            is Result.Success -> {
                binding.portalRankingsShimmer.apply {
                    hideShimmer()
                    stopShimmer()
                    gone()
                }
                rankingsAdapter.submitList(result.value)
            }
            is Result.Loading -> Unit
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