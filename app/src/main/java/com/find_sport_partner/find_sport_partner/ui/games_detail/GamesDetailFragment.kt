package com.find_sport_partner.find_sport_partner.ui.games_detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.find_sport_partner.find_sport_partner.databinding.FragmentGamesDetailBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class GamesDetailFragment : Fragment() {
    private var _binding: FragmentGamesDetailBinding? = null

    private val binding get() = _binding!!

    private val viewModel by viewModels<GamesDetailViewModel>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentGamesDetailBinding.inflate(inflater, container, false)
        val root = binding.root

        binding.topBar.setNavigationOnClickListener {
            viewModel.backClicked()
        }

        lifecycleScope.launchWhenStarted {
            viewModel.navigation.collect {
                when (it) {
                    GamesDetailContract.ViewInstructions.NavigateBack -> findNavController().navigateUp()
                }
            }
        }

        lifecycleScope.launchWhenCreated {
            viewModel.sportPartnerData.collectLatest {
                binding.tvSport.text = it.title
                binding.tvInfo.text = it.aditionalInfo
                binding.tvLocation.text = it.address
            }
        }


        return root
    }
}