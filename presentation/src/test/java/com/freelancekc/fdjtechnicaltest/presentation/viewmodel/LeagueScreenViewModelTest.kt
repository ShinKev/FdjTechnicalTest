package com.freelancekc.fdjtechnicaltest.presentation.viewmodel

import com.freelancekc.fdjtechnicaltest.domain.models.League
import com.freelancekc.fdjtechnicaltest.domain.models.Team
import com.freelancekc.fdjtechnicaltest.domain.usecase.GetAllLeaguesUseCase
import com.freelancekc.fdjtechnicaltest.domain.usecase.GetFilteredTeamsByLeagueUseCase
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import java.io.IOException

@ExperimentalCoroutinesApi
class LeagueScreenViewModelTest {

    private lateinit var getAllLeaguesUseCase: GetAllLeaguesUseCase
    private lateinit var getFilteredTeamsByLeagueUseCase: GetFilteredTeamsByLeagueUseCase
    private lateinit var viewModel: LeagueScreenViewModel

    private val testDispatcher = StandardTestDispatcher()

    private val testLeagues = listOf(
        League(id = "1", name = "Premier League"),
        League(id = "2", name = "La Liga"),
        League(id = "3", name = "Bundesliga"),
        League(id = "4", name = "Serie A"),
        League(id = "5", name = "Ligue 1")
    )

    private val testTeams = listOf(
        Team(id = "1", name = "Manchester United", imageUrl = "https://example.com/man_utd.png"),
        Team(id = "2", name = "Liverpool", imageUrl = "https://example.com/liverpool.png"),
        Team(id = "3", name = "Arsenal", imageUrl = "https://example.com/arsenal.png")
    )

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        getAllLeaguesUseCase = mockk()
        getFilteredTeamsByLeagueUseCase = mockk()
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    private fun createViewModelAndWaitForInitialization(): LeagueScreenViewModel {
        val viewModel = LeagueScreenViewModel(
            getAllLeaguesUseCase = getAllLeaguesUseCase,
            getFilteredTeamsByLeagueUseCase = getFilteredTeamsByLeagueUseCase
        )
        testDispatcher.scheduler.advanceUntilIdle()
        return viewModel
    }

    @Test
    fun `initial state should have correct default values`() = runTest {
        // Given
        coEvery { getAllLeaguesUseCase() } returns flowOf(Result.success(emptyList()))

        // When
        viewModel = createViewModelAndWaitForInitialization()

        // Then
        val currentState = viewModel.uiState.value
        assertEquals("", currentState.searchQuery)
        assertEquals(emptyList<League>(), currentState.leagues)
        assertEquals(emptyList<League>(), currentState.filteredLeagues)
        assertNull(currentState.selectedLeague)
        assertEquals(emptyList<Team>(), currentState.teams)
        assertFalse(currentState.isLoadingTeams)
        assertNull(currentState.leaguesError)
        assertNull(currentState.teamsError)
        assertFalse(currentState.showResults)
    }

    @Test
    fun `should load leagues successfully on initialization`() = runTest {
        // Given
        coEvery { getAllLeaguesUseCase() } returns flowOf(Result.success(testLeagues))

        // When
        viewModel = createViewModelAndWaitForInitialization()

        // Then
        val currentState = viewModel.uiState.value
        assertEquals(testLeagues, currentState.leagues)
        assertNull(currentState.leaguesError)
    }

    @Test
    fun `should handle leagues loading error`() = runTest {
        // Given
        val errorMessage = "Network error"
        val exception = IOException(errorMessage)
        coEvery { getAllLeaguesUseCase() } returns flowOf(Result.failure(exception))

        // When
        viewModel = createViewModelAndWaitForInitialization()

        // Then
        val currentState = viewModel.uiState.value
        assertEquals(emptyList<League>(), currentState.leagues)
        assertEquals(errorMessage, currentState.leaguesError)
    }

    @Test
    fun `onSearchQueryChange should update search query and filter leagues`() = runTest {
        // Given
        coEvery { getAllLeaguesUseCase() } returns flowOf(Result.success(testLeagues))
        viewModel = createViewModelAndWaitForInitialization()

        // When
        viewModel.onSearchQueryChange("Premier")

        // Then
        val currentState = viewModel.uiState.value
        assertEquals("Premier", currentState.searchQuery)
        assertEquals(listOf(testLeagues[0]), currentState.filteredLeagues)
        assertTrue(currentState.showResults)
    }

    @Test
    fun `onSearchQueryChange should show empty results for blank query`() = runTest {
        // Given
        coEvery { getAllLeaguesUseCase() } returns flowOf(Result.success(testLeagues))
        viewModel = createViewModelAndWaitForInitialization()

        // When
        viewModel.onSearchQueryChange("")

        // Then
        val currentState = viewModel.uiState.value
        assertEquals("", currentState.searchQuery)
        assertEquals(emptyList<League>(), currentState.filteredLeagues)
        assertFalse(currentState.showResults)
    }

    @Test
    fun `onSearchQueryChange should filter leagues case insensitively`() = runTest {
        // Given
        coEvery { getAllLeaguesUseCase() } returns flowOf(Result.success(testLeagues))
        viewModel = createViewModelAndWaitForInitialization()

        // When
        viewModel.onSearchQueryChange("liga")

        // Then
        val currentState = viewModel.uiState.value
        assertEquals("liga", currentState.searchQuery)
        assertEquals(2, currentState.filteredLeagues.size)
        assertTrue(currentState.filteredLeagues.contains(testLeagues[1])) // "La Liga"
        assertTrue(currentState.filteredLeagues.contains(testLeagues[2])) // "Bundesliga"
        assertTrue(currentState.showResults)
    }

    @Test
    fun `onSearchQueryChange should show multiple filtered results`() = runTest {
        // Given
        coEvery { getAllLeaguesUseCase() } returns flowOf(Result.success(testLeagues))
        viewModel = createViewModelAndWaitForInitialization()

        // When
        viewModel.onSearchQueryChange("L") // Should match leagues containing "L"

        // Then
        val currentState = viewModel.uiState.value
        assertEquals("L", currentState.searchQuery)
        assertEquals(4, currentState.filteredLeagues.size) // Premier League, La Liga, Bundesliga, Ligue 1
        assertTrue(currentState.filteredLeagues.contains(testLeagues[0])) // Premier League
        assertTrue(currentState.filteredLeagues.contains(testLeagues[1])) // La Liga
        assertTrue(currentState.filteredLeagues.contains(testLeagues[2])) // Bundesliga
        assertTrue(currentState.filteredLeagues.contains(testLeagues[4])) // Ligue 1
        assertTrue(currentState.showResults)
    }

    @Test
    fun `onLeagueSelected should update selected league and trigger team loading`() = runTest {
        // Given
        coEvery { getAllLeaguesUseCase() } returns flowOf(Result.success(testLeagues))
        coEvery { 
            getFilteredTeamsByLeagueUseCase(GetFilteredTeamsByLeagueUseCase.Params("Premier League")) 
        } returns flowOf(Result.success(testTeams))
        
        viewModel = createViewModelAndWaitForInitialization()

        // When
        viewModel.onLeagueSelected(testLeagues[0])
        testDispatcher.scheduler.advanceUntilIdle()

        // Then
        val currentState = viewModel.uiState.value
        assertEquals(testLeagues[0], currentState.selectedLeague)
        assertEquals("Premier League", currentState.searchQuery)
        assertFalse(currentState.showResults)
        assertEquals(emptyList<League>(), currentState.filteredLeagues)
        assertEquals(testTeams, currentState.teams)
        assertFalse(currentState.isLoadingTeams)
        assertNull(currentState.teamsError)
    }

    @Test
    fun `onLeagueSelected should not reload teams if same league is selected again`() = runTest {
        // Given
        coEvery { getAllLeaguesUseCase() } returns flowOf(Result.success(testLeagues))
        coEvery { 
            getFilteredTeamsByLeagueUseCase(GetFilteredTeamsByLeagueUseCase.Params("Premier League")) 
        } returns flowOf(Result.success(testTeams))
        
        viewModel = createViewModelAndWaitForInitialization()
        
        // When
        viewModel.onLeagueSelected(testLeagues[0])
        testDispatcher.scheduler.advanceUntilIdle()
        viewModel.onLeagueSelected(testLeagues[0])

        // Then
        val currentState = viewModel.uiState.value
        assertEquals(testLeagues[0], currentState.selectedLeague)
        assertEquals("Premier League", currentState.searchQuery)
        assertFalse(currentState.showResults)
        assertEquals(emptyList<League>(), currentState.filteredLeagues)
        assertEquals(testTeams, currentState.teams)
        assertFalse(currentState.isLoadingTeams)
    }

    @Test
    fun `should handle team loading error`() = runTest {
        // Given
        val errorMessage = "Failed to load teams"
        val exception = IOException(errorMessage)
        coEvery { getAllLeaguesUseCase() } returns flowOf(Result.success(testLeagues))
        coEvery { 
            getFilteredTeamsByLeagueUseCase(GetFilteredTeamsByLeagueUseCase.Params("Premier League")) 
        } returns flowOf(Result.failure(exception))
        
        viewModel = createViewModelAndWaitForInitialization()

        // When
        viewModel.onLeagueSelected(testLeagues[0])
        testDispatcher.scheduler.advanceUntilIdle()

        // Then
        val currentState = viewModel.uiState.value
        assertFalse(currentState.isLoadingTeams)
        assertEquals(errorMessage, currentState.teamsError)
        assertEquals(emptyList<Team>(), currentState.teams)
        assertEquals(testLeagues[0], currentState.selectedLeague)
    }

    @Test
    fun `onClearSearch should reset all search and selection state`() = runTest {
        // Given
        coEvery { getAllLeaguesUseCase() } returns flowOf(Result.success(testLeagues))
        coEvery { 
            getFilteredTeamsByLeagueUseCase(GetFilteredTeamsByLeagueUseCase.Params("Premier League")) 
        } returns flowOf(Result.success(testTeams))
        
        viewModel = createViewModelAndWaitForInitialization()
        viewModel.onSearchQueryChange("Premier")
        viewModel.onLeagueSelected(testLeagues[0])
        testDispatcher.scheduler.advanceUntilIdle()

        // When
        viewModel.onClearSearch()

        // Then
        val currentState = viewModel.uiState.value
        assertEquals("", currentState.searchQuery)
        assertFalse(currentState.showResults)
        assertEquals(emptyList<League>(), currentState.filteredLeagues)
        assertNull(currentState.selectedLeague)
        assertEquals(emptyList<Team>(), currentState.teams)
    }
}
