package com.find_sport_partner.find_sport_partner.ui.sport_partner_map

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.find_sport_partner.find_sport_partner.databinding.FragmentSportPartnerMapBinding
import com.find_sport_partner.find_sport_partner.extensions.toVisibility
import com.mapbox.maps.*
import com.mapbox.maps.plugin.animation.CameraAnimationsLifecycleListener
import com.mapbox.maps.plugin.animation.camera
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SportPartnerMapFragment() : Fragment() {

    private var _binding: FragmentSportPartnerMapBinding? = null

    private val binding get() = _binding!!

    private val viewModel by viewModels<SportPartnerMapViewModel>()

    private lateinit var mapView: MapView

    private val initCameraOptions = CameraOptions.Builder()
        .zoom(13.0)
        .build()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentSportPartnerMapBinding.inflate(inflater, container, false)

        mapView = binding.mvMap
        binding.ivPin.toVisibility = false

        mapView.camera.addCameraCenterChangeListener{
            binding.ivPin.toVisibility = true
        }

        viewModel.test.observe(viewLifecycleOwner, Observer {
            Log.e("THIS -_>", "$it")
        })

        return binding.root
    }

    override fun onStart() {
        mapView.onStart()
        super.onStart()
    }

    override fun onStop() {
        mapView.onStop()
        super.onStop()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mapView.onLowMemory()
    }

    override fun onDestroyView() {
        mapView.onDestroy()
        super.onDestroyView()
    }
}