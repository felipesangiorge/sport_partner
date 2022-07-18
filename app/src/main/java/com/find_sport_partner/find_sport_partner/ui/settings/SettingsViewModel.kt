package com.find_sport_partner.find_sport_partner.ui.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor() : ViewModel(), SettingsContract.ViewModel {

    private val _navigation = Channel<SettingsContract.ViewInstructions>()
    override val navigation: Flow<SettingsContract.ViewInstructions> = _navigation.receiveAsFlow()

    override fun backClicked() {
        viewModelScope.launch {
            _navigation.send(
                SettingsContract.ViewInstructions.NavigateBack
            )
        }
    }
}