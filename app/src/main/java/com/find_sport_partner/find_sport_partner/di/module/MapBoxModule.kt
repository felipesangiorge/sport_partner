package com.find_sport_partner.find_sport_partner.di.module

import android.content.Context
import com.find_sport_partner.find_sport_partner.R
import com.mapbox.geojson.Point
import com.mapbox.maps.CameraOptions
import com.mapbox.maps.ResourceOptions
import com.mapbox.search.*
import com.mapbox.search.record.IndexableDataProvider
import com.mapbox.search.record.IndexableRecord
import com.mapbox.search.result.SearchResult
import com.mapbox.search.result.SearchSuggestion
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import java.util.concurrent.Executor
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object MapBoxModule {

    @Provides
    @Singleton
    fun provideInitialMapBoxCameraConfig(): CameraOptions {
        return CameraOptions.Builder()
            .center(Point.fromLngLat(-74.0066, 40.7135))
            .pitch(0.0)
            .zoom(15.5)
            .bearing(-17.6)
            .build()
    }

    @Provides
    @Singleton
    fun provideInitialResourceOptions(@ApplicationContext context: Context): ResourceOptions {
        return ResourceOptions.Builder()
            .accessToken(context.getString(R.string.mapbox_access_token))
            .build()
    }
}