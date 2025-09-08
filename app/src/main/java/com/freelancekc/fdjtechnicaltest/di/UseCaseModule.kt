package com.freelancekc.fdjtechnicaltest.di

import com.freelancekc.fdjtechnicaltest.domain.repository.SportsRepository
import com.freelancekc.fdjtechnicaltest.domain.usecase.GetAllLeaguesUseCase
import com.freelancekc.fdjtechnicaltest.domain.usecase.GetFilteredTeamsByLeagueUseCase
import com.freelancekc.fdjtechnicaltest.domain.usecase.GetTeamsByLeagueUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object UseCaseModule {

    @Provides
    fun provideGetAllLeaguesUseCase(
        sportsRepository: SportsRepository
    ): GetAllLeaguesUseCase = GetAllLeaguesUseCase(sportsRepository)

    @Provides
    fun provideGetTeamsByLeagueUseCase(
        sportsRepository: SportsRepository
    ): GetTeamsByLeagueUseCase = GetTeamsByLeagueUseCase(sportsRepository)

    @Provides
    fun provideGetFilteredTeamsByLeagueUseCase(
        getTeamsByLeagueUseCase: GetTeamsByLeagueUseCase
    ): GetFilteredTeamsByLeagueUseCase = GetFilteredTeamsByLeagueUseCase(getTeamsByLeagueUseCase)
}

