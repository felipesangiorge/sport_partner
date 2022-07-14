package com.find_sport_partner.find_sport_partner.ui.sport_partner_map

import android.Manifest
import android.content.Context.LOCATION_SERVICE
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.core.location.LocationListenerCompat
import androidx.core.location.LocationManagerCompat
import androidx.core.location.LocationRequestCompat
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.find_sport_partner.find_sport_partner.R
import com.find_sport_partner.find_sport_partner.databinding.FragmentSportPartnerMapBinding
import com.find_sport_partner.find_sport_partner.domain.SportPartnerMarkerModel
import com.find_sport_partner.find_sport_partner.extensions.toVisibility
import com.mapbox.geojson.Point
import com.mapbox.maps.CameraOptions
import com.mapbox.maps.MapView
import com.mapbox.maps.ResourceOptions
import com.mapbox.maps.plugin.animation.MapAnimationOptions.Companion.mapAnimationOptions
import com.mapbox.maps.plugin.animation.camera
import com.mapbox.maps.plugin.animation.flyTo
import com.mapbox.maps.plugin.annotation.annotations
import com.mapbox.maps.plugin.annotation.generated.CircleAnnotationOptions
import com.mapbox.maps.plugin.annotation.generated.OnCircleAnnotationClickListener
import com.mapbox.maps.plugin.annotation.generated.createCircleAnnotationManager
import com.mapbox.search.*
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import javax.inject.Inject


@AndroidEntryPoint
class SportPartnerMapFragment() : Fragment() {

    private var _binding: FragmentSportPartnerMapBinding? = null

    private val binding get() = _binding!!

    private val viewModel by viewModels<SportPartnerMapViewModel>()

    private lateinit var mapView: MapView

    @Inject
    lateinit var locationPermissionRequest: ActivityResultLauncher<Array<out String>>

    @Inject
    lateinit var initialCameraOptions: CameraOptions

    @Inject
    lateinit var initialResourceOptions: ResourceOptions

    lateinit var searchEngine: SearchEngine

    var point: Point? = null


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentSportPartnerMapBinding.inflate(inflater, container, false)

        Log.e("SCREN1","SCREN1")

        binding.ivPin.toVisibility = false

        searchEngine = MapboxSearchSdk.getSearchEngine()

        locationPermissionRequest.launch(
            arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            )
        )

        val reverseSearchCallback = object : SearchCallback {
            override fun onError(e: Exception) {
                Log.e("Ex", "$e")
            }

            override fun onResults(results: List<com.mapbox.search.result.SearchResult>, responseInfo: ResponseInfo) {
                if (results.isNotEmpty()) {
                    val address = results.first().address
                    binding.etMapSearch.setText("${address?.place}, ${address?.locality.orEmpty()} ${address?.district.orEmpty()} , ${address?.country}")
                }
            }
        }

        binding.mvMap.getMapboxMap().flyTo(initialCameraOptions)
        mapView = binding.mvMap

        mapView.camera.addCameraCenterChangeListener {
            binding.ivPin.toVisibility = true
            point = it
        }


        mapView.getMapboxMap().addOnMapIdleListener {
            point?.let {
                binding.btnCreate.setOnClickListener { _ ->
                    viewModel.onCreateMarker(it)
                }

                searchEngine.search(
                    ReverseGeoOptions(
                        it
                    ), reverseSearchCallback
                )
            }
        }

        binding.nsBottomViewScroll.setOnTouchListener { view, motionEvent ->
            view.performClick()
        }

        binding.ivLocation.setOnClickListener {
            getLastBestLocation()?.let {
                val position = CameraOptions.Builder()
                    .center(Point.fromLngLat(it.longitude, it.latitude))
                    .zoom(16.0)
                    .bearing(0.0)
                    .build()

                mapView.getMapboxMap().flyTo(
                    position,
                    mapAnimationOptions {
                        duration(7000)
                    }
                )
            }
        }
        binding.etMapSearch.addTextChangedListener {
            viewModel.onMapSearchTextChange(it.toString())
        }

        binding.etTitle.addTextChangedListener {
            viewModel.titleTextChange(it.toString())
        }

        binding.etAditionalInfo.addTextChangedListener {
            viewModel.aditionalInformationTextChange(it.toString())
        }

        lifecycleScope.launchWhenStarted {

            viewModel.navigation.collectLatest {
                when (it) {
                    is SportPartnerMapContract.ViewInstructions.CreateMapPointMarker -> {
                        createMapBoxMarker(it.point, it.data)
                    }
                    is SportPartnerMapContract.ViewInstructions.NavigateToMapPointMarkerDetail -> {
                        val bundle = Bundle()
                        bundle.putParcelable("sportPartnerData", it.data)

                        findNavController().navigate(R.id.action_mapFragment_to_gamesFragment, bundle)
                    }
                    else -> {}
                }
            }

            viewModel.mapSearchText.collectLatest {
                if (it.orEmpty() != binding.etMapSearch.text.toString()) {
                    binding.etMapSearch.setText(it)
                }
            }

            viewModel.titleText.collectLatest {
                if (it.orEmpty() != binding.etTitle.text.toString()) {
                    binding.etTitle.setText(it)
                }
            }

            viewModel.aditionalInformationText.collectLatest {
                if (it.orEmpty() != binding.etAditionalInfo.text.toString()) {
                    binding.etAditionalInfo.setText(it)
                }
            }
        }

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

    private fun createMapBoxMarker(point: Point, data: SportPartnerMarkerModel) {
        val position = CameraOptions.Builder()
            .center(point)
            .build()

        // Create an instance of the Annotation API and get the CircleAnnotationManager.
        val annotationApi = mapView.annotations
        val circleAnnotationManager = annotationApi.createCircleAnnotationManager()
        // Set options for the resulting circle layer.
        val circleAnnotationOptions: CircleAnnotationOptions = CircleAnnotationOptions()
            // Define a geographic coordinate.
            .withPoint(point)
            // Style the circle that will be added to the map.
            .withCircleRadius(12.0)
            .withCircleColor("#ee4e8b")
            .withCircleStrokeWidth(2.0)
            .withCircleStrokeColor("#ffffff")
        // Add the resulting circle to the map.
        circleAnnotationManager
            .addClickListener(OnCircleAnnotationClickListener {
                mapView.getMapboxMap().flyTo(
                    position
                )

                viewModel.navigateToSportDetail(data)

                binding.ivPin.toVisibility = false

                true
            })

        circleAnnotationManager.create(circleAnnotationOptions)
    }

    private fun getLastBestLocation(): Location? {
        lateinit var location: Location

        val mgr = requireActivity().getSystemService(LOCATION_SERVICE) as LocationManager

        val mLocationListener = LocationListenerCompat {
            location = it
        }

        if (mgr.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            LocationManagerCompat.requestLocationUpdates(
                mgr,
                LOCATION_REFRESH_DISTANCE,
                LocationRequestCompat.Builder(LOCATION_REFRESH_TIME).build(),
                mLocationListener,
                Looper.getMainLooper()
            )
        }

        return location
    }

    companion object {
        const val LOCATION_REFRESH_TIME = 15000L // 15 seconds to update
        const val LOCATION_REFRESH_DISTANCE = "500" // 500 meters to update
    }
}