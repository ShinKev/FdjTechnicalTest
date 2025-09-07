package com.freelancekc.fdjtechnicaltest.domain.usecase

import app.cash.turbine.test
import com.freelancekc.fdjtechnicaltest.domain.models.Team
import com.freelancekc.fdjtechnicaltest.domain.repository.SportsRepository
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import java.io.IOException

@ExperimentalCoroutinesApi
class GetTeamsByLeagueUseCaseTest {

    private lateinit var sportsRepository: SportsRepository
    private lateinit var useCase: GetTeamsByLeagueUseCase

    private val testLeagueName = "Premier League"
    private val testTeams = listOf(
        Team(id = "1", name = "Manchester United", imageUrl = "https://example.com/man_utd.png"),
        Team(id = "2", name = "Liverpool", imageUrl = "https://example.com/liverpool.png"),
        Team(id = "3", name = "Arsenal", imageUrl = "https://example.com/arsenal.png")
    )

    @Before
    fun setUp() {
        sportsRepository = mockk()
        useCase = GetTeamsByLeagueUseCase(sportsRepository)
    }

    @Test
    fun `invoke returns success result when repository succeeds`() = runTest {
        // Given
        val params = GetTeamsByLeagueUseCase.Params(testLeagueName)
        coEvery { 
            sportsRepository.getTeamsByLeague(testLeagueName) 
        } returns flowOf(Result.success(testTeams))

        // When & Then
        useCase(params).test {
            val result = awaitItem()
            assertTrue(result.isSuccess)
            assertEquals(testTeams, result.getOrNull())
            awaitComplete()
        }
    }

    @Test
    fun `invoke returns failure result when repository fails`() = runTest {
        // Given
        val params = GetTeamsByLeagueUseCase.Params(testLeagueName)
        val exception = IOException("Network error")
        coEvery { 
            sportsRepository.getTeamsByLeague(testLeagueName) 
        } returns flowOf(Result.failure(exception))

        // When & Then
        useCase(params).test {
            val result = awaitItem()
            assertTrue(result.isFailure)
            assertNotNull(result.exceptionOrNull())
            assertEquals(exception, result.exceptionOrNull())
            awaitComplete()
        }
    }

    @Test
    fun `invoke returns empty list when no teams found for league`() = runTest {
        // Given
        val params = GetTeamsByLeagueUseCase.Params(testLeagueName)
        coEvery { 
            sportsRepository.getTeamsByLeague(testLeagueName) 
        } returns flowOf(Result.success(emptyList()))

        // When & Then
        useCase(params).test {
            val result = awaitItem()
            assertTrue(result.isSuccess)
            assertEquals(emptyList<Team>(), result.getOrNull())
            awaitComplete()
        }
    }

    @Test
    fun `invoke handles different league names correctly`() = runTest {
        // Given
        val laLigaTeams = listOf(
            Team(id = "10", name = "Real Madrid", imageUrl = "https://example.com/real_madrid.png"),
            Team(id = "11", name = "Barcelona", imageUrl = "https://example.com/barcelona.png")
        )
        val laLigaParams = GetTeamsByLeagueUseCase.Params("La Liga")

        coEvery {
            sportsRepository.getTeamsByLeague(testLeagueName)
        } returns flowOf(Result.success(testTeams))
        coEvery { 
            sportsRepository.getTeamsByLeague("La Liga") 
        } returns flowOf(Result.success(laLigaTeams))

        // When & Then
        useCase(laLigaParams).test {
            val result = awaitItem()
            assertTrue(result.isSuccess)
            assertEquals(laLigaTeams, result.getOrNull())
            awaitComplete()
        }
    }
}
