package com.find_sport_partner.find_sport_partner.ui.settings

import kotlinx.coroutines.flow.Flow

interface SettingsContract {

    interface ViewModel: ViewState, ViewActions

    interface ViewState{
        val navigation: Flow<ViewInstructions>
    }

    interface ViewActions{
        fun backClicked()
    }

    sealed class ViewInstructions{
        object NavigateBack: ViewInstructions()
    }
}