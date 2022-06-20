package com.find_sport_partner.find_sport_partner.ui.sport_partner_map

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.find_sport_partner.find_sport_partner.data.repository.SportPartnerMapRepository_Imp
import com.find_sport_partner.find_sport_partner.domain.SportPartnerMarkerModel
import com.mapbox.geojson.Point
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


@HiltViewModel
class SportPartnerMapViewModel @Inject constructor(
    sportPartnerMapRepository: SportPartnerMapRepository_Imp,
) : ViewModel(), SportPartnerMapContract.ViewModel {

    private var _navigation = MediatorLiveData<SportPartnerMapContract.ViewInstructions>()
    override val navigation: LiveData<SportPartnerMapContract.ViewInstructions> = _navigation

    private var _mapSearchText = MutableLiveData<String>()
    override val mapSearchText: LiveData<String> = _mapSearchText

    private var _titleText = MutableLiveData<String>()
    override val titleText: LiveData<String> = _titleText

    private var _aditionalInformationText = MutableLiveData<String>()
    override val aditionalInformationText: LiveData<String> = _aditionalInformationText


    init {
    }

    override fun onMapSearchTextChange(input: String) {
        if (_mapSearchText.value == input) return
        _mapSearchText.value = input
    }

    override fun titleTextChange(input: String) {
        if (_titleText.value == input) return
        _titleText.value = input
    }

    override fun aditionalInformationTextChange(input: String) {
        if (_aditionalInformationText.value == input) return
        _aditionalInformationText.value = input
    }

    override fun onCreateMarker(point: Point) {
        val milis = System.currentTimeMillis()
        _navigation.value = SportPartnerMapContract.ViewInstructions.CreateMapPointMarker(
            point,
            SportPartnerMarkerModel(
                milis.toString(),
                _titleText.value.orEmpty(),
                _aditionalInformationText.value
            )
        )
    }
}