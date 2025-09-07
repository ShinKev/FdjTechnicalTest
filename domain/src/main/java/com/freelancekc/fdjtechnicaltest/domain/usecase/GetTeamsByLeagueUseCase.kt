package com.freelancekc.fdjtechnicaltest.domain.usecase

import com.freelancekc.fdjtechnicaltest.domain.models.Team
import com.freelancekc.fdjtechnicaltest.domain.repository.SportsRepository
import kotlinx.coroutines.flow.Flow

class GetTeamsByLeagueUseCase(
    private val sportsRepository: SportsRepository
) : FlowUseCase<GetTeamsByLeagueUseCase.Params, List<Team>>() {

    override suspend fun execute(parameters: Params): Flow<Result<List<Team>>> {
        return sportsRepository.getTeamsByLeague(parameters.leagueName)
    }

    data class Params(
        val leagueName: String
    )
}
