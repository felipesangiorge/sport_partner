package com.find_sport_partner.find_sport_partner.ui.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor() : ViewModel(), ProfileContract.ViewModel {

    private val _navigation = Channel<ProfileContract.ViewInstructions>()
    override val navigation: Flow<ProfileContract.ViewInstructions> = _navigation.receiveAsFlow()

    override fun settingsClicked() {
        viewModelScope.launch {
            _navigation.send(ProfileContract.ViewInstructions.NavigateToSettings)
        }
    }
}