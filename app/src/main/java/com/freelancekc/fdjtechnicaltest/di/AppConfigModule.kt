package com.freelancekc.fdjtechnicaltest.di

import com.freelancekc.fdjtechnicaltest.BuildConfig
import com.freelancekc.fdjtechnicaltest.data.config.NetworkConfig
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppConfigModule {

    @Provides
    @Singleton
    fun provideNetworkConfig(): NetworkConfig = NetworkConfig(
        baseUrl = BuildConfig.BASE_URL,
        isDebug = BuildConfig.DEBUG
    )
}
