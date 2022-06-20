package com.find_sport_partner.find_sport_partner.ui.sport_partner_map

import androidx.lifecycle.LiveData

interface SportPartnerMapContract {

    interface ViewModel : ViewState, ViewActions

    interface ViewState {
        val test: LiveData<String>
    }

    interface ViewActions {

    }
}