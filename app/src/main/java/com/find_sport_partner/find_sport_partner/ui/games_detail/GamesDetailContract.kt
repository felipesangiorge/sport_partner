package com.find_sport_partner.find_sport_partner.ui.games_detail

import com.find_sport_partner.find_sport_partner.domain.SportPartnerMarkerModel
import kotlinx.coroutines.flow.Flow

interface GamesDetailContract {

    interface ViewModel: ViewState, ViewActions

    interface ViewState{
        val navigation: Flow<ViewInstructions>
        val sportPartnerData: Flow<SportPartnerMarkerModel>
    }

    interface ViewActions{
        fun backClicked()
    }

    sealed class ViewInstructions{
        object NavigateBack: ViewInstructions()
    }
}