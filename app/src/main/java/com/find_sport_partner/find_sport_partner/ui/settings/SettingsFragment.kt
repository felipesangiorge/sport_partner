package com.find_sport_partner.find_sport_partner.ui.settings

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.find_sport_partner.find_sport_partner.databinding.FragmentSettingsBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class SettingsFragment : Fragment() {

    private var _binding: FragmentSettingsBinding? = null

    private val binding get() = _binding!!

    private val viewModel by viewModels<SettingsViewModel>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        val root = binding.root

        binding.topBar.setNavigationOnClickListener {
            viewModel.backClicked()
        }

        lifecycleScope.launchWhenStarted {
            viewModel.navigation.collect {
                when (it) {
                    SettingsContract.ViewInstructions.NavigateBack -> {
                        Log.e("BACK CALL", "BACK CALL")
                        findNavController().navigateUp()
                    }
                }
            }
        }

        return root
    }
}