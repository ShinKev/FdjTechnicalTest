package com.freelancekc.fdjtechnicaltest.domain.usecase

import app.cash.turbine.test
import com.freelancekc.fdjtechnicaltest.domain.models.League
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
class GetAllLeaguesUseCaseTest {

    private lateinit var sportsRepository: SportsRepository
    private lateinit var useCase: GetAllLeaguesUseCase

    private val testLeagues = listOf(
        League(id = "1", name = "Premier League"),
        League(id = "2", name = "La Liga"),
        League(id = "3", name = "Bundesliga")
    )

    @Before
    fun setUp() {
        sportsRepository = mockk()
        useCase = GetAllLeaguesUseCase(sportsRepository)
    }

    @Test
    fun `invoke returns success result when repository succeeds`() = runTest {
        // Given
        coEvery { sportsRepository.getAllLeagues() } returns flowOf(Result.success(testLeagues))

        // When & Then
        useCase().test {
            val result = awaitItem()
            assertTrue(result.isSuccess)
            assertEquals(testLeagues, result.getOrNull())
            awaitComplete()
        }
    }

    @Test
    fun `invoke returns failure result when repository fails`() = runTest {
        // Given
        val exception = IOException("Network error")
        coEvery { sportsRepository.getAllLeagues() } returns flowOf(Result.failure(exception))

        // When & Then
        useCase().test {
            val result = awaitItem()
            assertTrue(result.isFailure)
            assertNotNull(result.exceptionOrNull())
            assertEquals(exception, result.exceptionOrNull())
            awaitComplete()
        }
    }

    @Test
    fun `invoke returns empty list when repository returns empty result`() = runTest {
        // Given
        coEvery { sportsRepository.getAllLeagues() } returns flowOf(Result.success(emptyList()))

        // When & Then
        useCase().test {
            val result = awaitItem()
            assertTrue(result.isSuccess)
            assertEquals(emptyList<League>(), result.getOrNull())
            awaitComplete()
        }
    }
}
