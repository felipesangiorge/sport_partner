package com.find_sport_partner.find_sport_partner.di.module

import android.content.Context
import com.find_sport_partner.find_sport_partner.BaseApplication
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun contributeActivityInjector(@ApplicationContext app: Context): BaseApplication = app as BaseApplication
}