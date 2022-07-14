package com.find_sport_partner.find_sport_partner.ui.sport_partner_map

import androidx.lifecycle.LiveData
import com.find_sport_partner.find_sport_partner.domain.SportPartnerMarkerModel
import com.mapbox.geojson.Point
import kotlinx.coroutines.flow.Flow

interface SportPartnerMapContract {

    interface ViewModel : ViewState, ViewActions

    interface ViewState {
        val navigation: Flow<ViewInstructions>
        val mapSearchText: Flow<String>
        val titleText: Flow<String>
        val aditionalInformationText: Flow<String>
    }

    interface ViewActions {
        fun onMapSearchTextChange(input: String)
        fun titleTextChange(input: String)
        fun aditionalInformationTextChange(input: String)

        fun onCreateMarker(point: Point)

        fun navigateToSportDetail(data: SportPartnerMarkerModel)
    }

    sealed class ViewInstructions{
        data class CreateMapPointMarker(val point: Point, val data: SportPartnerMarkerModel): ViewInstructions()
        data class NavigateToMapPointMarkerDetail(val data: SportPartnerMarkerModel): ViewInstructions()
    }
}