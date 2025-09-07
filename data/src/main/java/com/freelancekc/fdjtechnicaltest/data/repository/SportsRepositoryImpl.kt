package com.freelancekc.fdjtechnicaltest.data.repository

import com.freelancekc.fdjtechnicaltest.data.api.SportsApiService
import com.freelancekc.fdjtechnicaltest.data.models.toLeagueDomain
import com.freelancekc.fdjtechnicaltest.data.models.toTeamDomain
import com.freelancekc.fdjtechnicaltest.domain.models.League
import com.freelancekc.fdjtechnicaltest.domain.models.Team
import com.freelancekc.fdjtechnicaltest.domain.repository.SportsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SportsRepositoryImpl @Inject constructor(
    private val apiService: SportsApiService
) : SportsRepository {

    override suspend fun getAllLeagues(): Flow<Result<List<League>>> = flow {
        try {
            val response = apiService.getAllLeagues()
            val leagues = response.leagues.toLeagueDomain()
            emit(Result.success(leagues))
        } catch (exception: Exception) {
            emit(Result.failure(exception))
        }
    }

    override suspend fun getTeamsByLeague(leagueName: String): Flow<Result<List<Team>>> = flow {
        try {
            val response = apiService.getTeamsByLeague(leagueName)
            val teams = response.teams.toTeamDomain()
            emit(Result.success(teams))
        } catch (exception: Exception) {
            emit(Result.failure(exception))
        }
    }
}
