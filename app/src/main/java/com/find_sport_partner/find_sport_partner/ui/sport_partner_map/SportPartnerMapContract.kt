package com.find_sport_partner.find_sport_partner.ui.sport_partner_map

import androidx.lifecycle.LiveData
import com.find_sport_partner.find_sport_partner.domain.SportPartnerMarkerModel
import com.mapbox.geojson.Point

interface SportPartnerMapContract {

    interface ViewModel : ViewState, ViewActions

    interface ViewState {
        val navigation: LiveData<ViewInstructions>
        val mapSearchText: LiveData<String>
        val titleText: LiveData<String>
        val aditionalInformationText: LiveData<String>
    }

    interface ViewActions {
        fun onMapSearchTextChange(input: String)
        fun titleTextChange(input: String)
        fun aditionalInformationTextChange(input: String)

        fun onCreateMarker(point: Point)
    }

    sealed class ViewInstructions{
        data class CreateMapPointMarker(val point: Point, val data: SportPartnerMarkerModel): ViewInstructions()
    }
}