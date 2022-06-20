package com.find_sport_partner.find_sport_partner.ui.sport_partner_map

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.find_sport_partner.find_sport_partner.data.repository.SportPartnerMapRepository_Imp
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


@HiltViewModel
class SportPartnerMapViewModel @Inject constructor(
    sportPartnerMapRepository: SportPartnerMapRepository_Imp,
) : ViewModel(), SportPartnerMapContract.ViewModel {

    private val _test = MutableLiveData("TESTEEE")
    override val test: LiveData<String> = _test
}