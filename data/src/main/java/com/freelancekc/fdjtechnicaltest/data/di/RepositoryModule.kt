package com.freelancekc.fdjtechnicaltest.data.di

import com.freelancekc.fdjtechnicaltest.data.repository.SportsRepositoryImpl
import com.freelancekc.fdjtechnicaltest.domain.repository.SportsRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindSportsRepository(
        sportsRepositoryImpl: SportsRepositoryImpl
    ): SportsRepository
}
