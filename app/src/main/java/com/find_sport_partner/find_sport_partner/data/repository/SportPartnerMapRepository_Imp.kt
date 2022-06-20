package com.find_sport_partner.find_sport_partner.data.repository

import com.find_sport_partner.find_sport_partner.data.network.SportPartnerService
import javax.inject.Inject


class SportPartnerMapRepository_Imp @Inject constructor(
    sportPartnerService: SportPartnerService
) : SportPartnerMapRepository {
    override fun test() {
        TODO("Not yet implemented")
    }
}