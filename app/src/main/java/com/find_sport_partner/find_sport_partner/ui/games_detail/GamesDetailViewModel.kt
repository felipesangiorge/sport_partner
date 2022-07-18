package com.find_sport_partner.find_sport_partner.ui.games_detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.find_sport_partner.find_sport_partner.domain.SportPartnerMarkerModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class GamesDetailViewModel @Inject constructor(
    private val state: SavedStateHandle
) : ViewModel(), GamesDetailContract.ViewModel {
    private val sportPartnerArg = state.get<SportPartnerMarkerModel>("sportPartnerData")!!

    private val _navigation = Channel<GamesDetailContract.ViewInstructions>()
    override val navigation: Flow<GamesDetailContract.ViewInstructions> = _navigation.receiveAsFlow()

    private val _sportPartnerData = MutableStateFlow(sportPartnerArg)
    override val sportPartnerData: Flow<SportPartnerMarkerModel> = _sportPartnerData

    override fun backClicked() {
        viewModelScope.launch {
            _navigation.send(GamesDetailContract.ViewInstructions.NavigateBack)
        }
    }
}