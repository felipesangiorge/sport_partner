package com.find_sport_partner.find_sport_partner.ui.profile

import kotlinx.coroutines.flow.Flow

interface ProfileContract {

    interface ViewModel : ViewState, ViewActions

    interface ViewState {
        val navigation: Flow<ViewInstructions>
    }

    interface ViewActions {
        fun settingsClicked()
    }

    sealed class ViewInstructions {
        object NavigateToSettings : ViewInstructions()
    }
}