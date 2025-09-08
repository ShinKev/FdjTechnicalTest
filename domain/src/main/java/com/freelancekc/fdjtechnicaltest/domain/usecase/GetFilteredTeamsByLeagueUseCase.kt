package com.freelancekc.fdjtechnicaltest.domain.usecase

import com.freelancekc.fdjtechnicaltest.domain.models.Team
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

/**
 * Use case that gets teams for a league, orders them Z to A, and returns one team out of two
 */
class GetFilteredTeamsByLeagueUseCase(
    private val getTeamsByLeagueUseCase: GetTeamsByLeagueUseCase
) : FlowUseCase<GetFilteredTeamsByLeagueUseCase.Params, List<Team>>() {

    override suspend fun execute(parameters: Params): Flow<Result<List<Team>>> {
        return getTeamsByLeagueUseCase(
            GetTeamsByLeagueUseCase.Params(parameters.leagueName)
        ).map { result ->
            result.map { teams ->
                teams
                    .sortedByDescending { it.name } // Sort Z to A (reverse alphabetical)
                    .filterIndexed { index, _ -> index % 2 == 0 } // Take one team out of two (0, 2, 4, ...)
            }
        }
    }

    data class Params(
        val leagueName: String
    )
}
