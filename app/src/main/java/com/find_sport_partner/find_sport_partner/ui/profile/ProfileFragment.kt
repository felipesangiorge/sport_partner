package com.find_sport_partner.find_sport_partner.ui.profile

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.find_sport_partner.find_sport_partner.R
import com.find_sport_partner.find_sport_partner.databinding.FragmentProfileBinding
import kotlinx.coroutines.flow.collectLatest

class ProfileFragment : Fragment() {
    private var _binding: FragmentProfileBinding? = null

    private val binding get() = _binding!!

    private val viewModel by viewModels<ProfileViewModel>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        val root = binding.root

        binding.ivSettings.setOnClickListener {
            viewModel.settingsClicked()
        }

        lifecycleScope.launchWhenStarted {
            viewModel.navigation.collectLatest {
                when (it) {
                    ProfileContract.ViewInstructions.NavigateToSettings -> {
                        Log.e("NAV CALL", "NAV CALL")
                        findNavController().navigate(R.id.action_profileFragment_to_settingsFragment)
                    }
                }
            }
        }

        return root
    }
}