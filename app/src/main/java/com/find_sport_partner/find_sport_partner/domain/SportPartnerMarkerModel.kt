package com.find_sport_partner.find_sport_partner.domain

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class SportPartnerMarkerModel(
    val id: String,
    val title: String,
    val aditionalInfo: String?,
    val address: String?
): DomainModel, Parcelable