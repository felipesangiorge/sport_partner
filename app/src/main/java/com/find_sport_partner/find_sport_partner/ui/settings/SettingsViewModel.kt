package com.find_sport_partner.find_sport_partner.ui.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor() : ViewModel(), SettingsContract.ViewModel {

    private val _navigation = MutableSharedFlow<SettingsContract.ViewInstructions>()
    override val navigation: Flow<SettingsContract.ViewInstructions> = _navigation

    override fun backClicked() {
        viewModelScope.launch {
            _navigation.emit(
                SettingsContract.ViewInstructions.NavigateBack
            )
        }
    }
}