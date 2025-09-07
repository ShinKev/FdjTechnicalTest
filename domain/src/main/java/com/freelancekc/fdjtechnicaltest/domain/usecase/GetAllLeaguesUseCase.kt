package com.freelancekc.fdjtechnicaltest.domain.usecase

import com.freelancekc.fdjtechnicaltest.domain.models.League
import com.freelancekc.fdjtechnicaltest.domain.repository.SportsRepository
import kotlinx.coroutines.flow.Flow

class GetAllLeaguesUseCase(
    private val sportsRepository: SportsRepository
) : NoParameterFlowUseCase<List<League>>() {

    override suspend fun execute(): Flow<Result<List<League>>> {
        return sportsRepository.getAllLeagues()
    }
}
