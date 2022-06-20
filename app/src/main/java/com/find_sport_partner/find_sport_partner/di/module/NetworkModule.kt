package com.find_sport_partner.find_sport_partner.di.module

import android.content.Context
import com.find_sport_partner.find_sport_partner.data.network.ApiRequest
import com.find_sport_partner.find_sport_partner.data.network.ServiceGenerator
import com.find_sport_partner.find_sport_partner.data.network.SportPartnerService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Singleton
    @Provides
    fun provideSportPartnerService(@ApplicationContext context: Context): SportPartnerService {
        return SportPartnerService(ServiceGenerator().serviceCreator(ApiRequest.SportPartnerApi::class.java, context))
    }
}