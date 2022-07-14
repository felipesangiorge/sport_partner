package com.find_sport_partner.find_sport_partner.ui.games_list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.find_sport_partner.find_sport_partner.databinding.FragmentGamesListBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class GamesListFragment() : Fragment() {

    private var _binding: FragmentGamesListBinding? = null

    private val binding get() = _binding!!

    private val viewModel by viewModels<GamesListViewModel>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentGamesListBinding.inflate(inflater, container, false)
        val root = binding.root


        return root
    }
}