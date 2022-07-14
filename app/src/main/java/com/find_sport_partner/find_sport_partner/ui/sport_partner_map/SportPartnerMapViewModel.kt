package com.find_sport_partner.find_sport_partner.ui.sport_partner_map

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.find_sport_partner.find_sport_partner.data.repository.SportPartnerMapRepository_Imp
import com.find_sport_partner.find_sport_partner.domain.SportPartnerMarkerModel
import com.mapbox.geojson.Point
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class SportPartnerMapViewModel @Inject constructor(
    sportPartnerMapRepository: SportPartnerMapRepository_Imp,
) : ViewModel(), SportPartnerMapContract.ViewModel {

    private var _navigation = MutableSharedFlow<SportPartnerMapContract.ViewInstructions>()
    override val navigation: Flow<SportPartnerMapContract.ViewInstructions> = _navigation.asSharedFlow()

    private val _mapSearchText = MutableStateFlow<String>("")
    override val mapSearchText: Flow<String> = _mapSearchText

    private var _titleText = MutableStateFlow<String>("")
    override val titleText: Flow<String> = _titleText

    private var _aditionalInformationText = MutableStateFlow<String>("")
    override val aditionalInformationText: Flow<String> = _aditionalInformationText


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

        viewModelScope.launch {
            _navigation.emit(
                SportPartnerMapContract.ViewInstructions.CreateMapPointMarker(
                    point,
                    SportPartnerMarkerModel(
                        milis.toString(),
                        _titleText.first(),
                        _aditionalInformationText.first(),
                        _mapSearchText.first()
                    )
                )
            )
        }
    }

    override fun navigateToSportDetail(data: SportPartnerMarkerModel) {
        viewModelScope.launch {
            _navigation.emit(
                SportPartnerMapContract.ViewInstructions.NavigateToMapPointMarkerDetail(data)
            )
        }
    }
}