package com.freelancekc.fdjtechnicaltest.domain.usecase

import app.cash.turbine.test
import com.freelancekc.fdjtechnicaltest.domain.models.Team
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
class GetFilteredTeamsByLeagueUseCaseTest {

    private lateinit var getTeamsByLeagueUseCase: GetTeamsByLeagueUseCase
    private lateinit var useCase: GetFilteredTeamsByLeagueUseCase

    private val testLeagueName = "Premier League"
    private val testTeams = listOf(
        Team(id = "1", name = "Arsenal", imageUrl = "https://example.com/arsenal.png"),
        Team(id = "2", name = "Brighton", imageUrl = "https://example.com/brighton.png"),
        Team(id = "3", name = "Chelsea", imageUrl = "https://example.com/chelsea.png"),
        Team(id = "4", name = "Liverpool", imageUrl = "https://example.com/liverpool.png"),
        Team(id = "5", name = "Manchester United", imageUrl = "https://example.com/man_utd.png"),
        Team(id = "6", name = "Tottenham", imageUrl = "https://example.com/tottenham.png")
    )

    @Before
    fun setUp() {
        getTeamsByLeagueUseCase = mockk()
        useCase = GetFilteredTeamsByLeagueUseCase(getTeamsByLeagueUseCase)
    }

    @Test
    fun `invoke returns teams sorted Z to A with every other team when repository succeeds`() = runTest {
        // Given
        val params = GetFilteredTeamsByLeagueUseCase.Params(testLeagueName)
        coEvery { 
            getTeamsByLeagueUseCase(GetTeamsByLeagueUseCase.Params(testLeagueName)) 
        } returns flowOf(Result.success(testTeams))

        val expectedFilteredTeams = listOf(
            Team(id = "6", name = "Tottenham", imageUrl = "https://example.com/tottenham.png"),
            Team(id = "4", name = "Liverpool", imageUrl = "https://example.com/liverpool.png"),
            Team(id = "2", name = "Brighton", imageUrl = "https://example.com/brighton.png")
        )

        // When & Then
        useCase(params).test {
            val result = awaitItem()
            assertTrue(result.isSuccess)
            assertEquals(expectedFilteredTeams, result.getOrNull())
            awaitComplete()
        }
    }

    @Test
    fun `invoke returns failure result when underlying use case fails`() = runTest {
        // Given
        val params = GetFilteredTeamsByLeagueUseCase.Params(testLeagueName)
        val exception = IOException("Network error")
        coEvery { 
            getTeamsByLeagueUseCase(GetTeamsByLeagueUseCase.Params(testLeagueName)) 
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
        val params = GetFilteredTeamsByLeagueUseCase.Params(testLeagueName)
        coEvery { 
            getTeamsByLeagueUseCase(GetTeamsByLeagueUseCase.Params(testLeagueName)) 
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
    fun `invoke handles single team correctly`() = runTest {
        // Given
        val singleTeam = listOf(
            Team(id = "1", name = "Arsenal", imageUrl = "https://example.com/arsenal.png")
        )
        val params = GetFilteredTeamsByLeagueUseCase.Params(testLeagueName)
        coEvery { 
            getTeamsByLeagueUseCase(GetTeamsByLeagueUseCase.Params(testLeagueName)) 
        } returns flowOf(Result.success(singleTeam))

        val expectedResult = singleTeam

        // When & Then
        useCase(params).test {
            val result = awaitItem()
            assertTrue(result.isSuccess)
            assertEquals(expectedResult, result.getOrNull())
            awaitComplete()
        }
    }

    @Test
    fun `invoke handles two teams correctly`() = runTest {
        // Given
        val twoTeams = listOf(
            Team(id = "1", name = "Arsenal", imageUrl = "https://example.com/arsenal.png"),
            Team(id = "2", name = "Zenit", imageUrl = "https://example.com/zenit.png")
        )
        val params = GetFilteredTeamsByLeagueUseCase.Params(testLeagueName)
        coEvery { 
            getTeamsByLeagueUseCase(GetTeamsByLeagueUseCase.Params(testLeagueName)) 
        } returns flowOf(Result.success(twoTeams))

        val expectedResult = listOf(
            Team(id = "2", name = "Zenit", imageUrl = "https://example.com/zenit.png")
        )

        // When & Then
        useCase(params).test {
            val result = awaitItem()
            assertTrue(result.isSuccess)
            assertEquals(expectedResult, result.getOrNull())
            awaitComplete()
        }
    }

    @Test
    fun `invoke handles odd number of teams correctly`() = runTest {
        // Given
        val fiveTeams = listOf(
            Team(id = "1", name = "Arsenal", imageUrl = "https://example.com/arsenal.png"),
            Team(id = "2", name = "Brighton", imageUrl = "https://example.com/brighton.png"),
            Team(id = "3", name = "Chelsea", imageUrl = "https://example.com/chelsea.png"),
            Team(id = "4", name = "Liverpool", imageUrl = "https://example.com/liverpool.png"),
            Team(id = "5", name = "Manchester United", imageUrl = "https://example.com/man_utd.png")
        )
        val params = GetFilteredTeamsByLeagueUseCase.Params(testLeagueName)
        coEvery { 
            getTeamsByLeagueUseCase(GetTeamsByLeagueUseCase.Params(testLeagueName)) 
        } returns flowOf(Result.success(fiveTeams))


        val expectedResult = listOf(
            Team(id = "5", name = "Manchester United", imageUrl = "https://example.com/man_utd.png"),
            Team(id = "3", name = "Chelsea", imageUrl = "https://example.com/chelsea.png"),
            Team(id = "1", name = "Arsenal", imageUrl = "https://example.com/arsenal.png")
        )

        // When & Then
        useCase(params).test {
            val result = awaitItem()
            assertTrue(result.isSuccess)
            assertEquals(expectedResult, result.getOrNull())
            awaitComplete()
        }
    }
}
