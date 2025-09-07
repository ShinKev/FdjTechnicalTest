package com.freelancekc.fdjtechnicaltest.domain.repository

import com.freelancekc.fdjtechnicaltest.domain.models.League
import com.freelancekc.fdjtechnicaltest.domain.models.Team
import kotlinx.coroutines.flow.Flow

interface SportsRepository {
    
    suspend fun getAllLeagues(): Flow<Result<List<League>>>
    
    suspend fun getTeamsByLeague(leagueName: String): Flow<Result<List<Team>>>
}
