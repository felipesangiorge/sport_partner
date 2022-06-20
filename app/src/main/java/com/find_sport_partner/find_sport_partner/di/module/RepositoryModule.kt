package com.find_sport_partner.find_sport_partner.di.module

import com.find_sport_partner.find_sport_partner.data.network.SportPartnerService
import com.find_sport_partner.find_sport_partner.data.repository.SportPartnerMapRepository
import com.find_sport_partner.find_sport_partner.data.repository.SportPartnerMapRepository_Imp
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Singleton
    @Provides
    fun sportPartnerMapRepository(
        sportPartnerService: SportPartnerService
    ): SportPartnerMapRepository = SportPartnerMapRepository_Imp(sportPartnerService)
}