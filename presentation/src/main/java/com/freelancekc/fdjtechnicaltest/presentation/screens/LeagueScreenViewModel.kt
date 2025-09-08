package com.freelancekc.fdjtechnicaltest.presentation.screens

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.freelancekc.fdjtechnicaltest.domain.models.League
import com.freelancekc.fdjtechnicaltest.domain.usecase.GetAllLeaguesUseCase
import com.freelancekc.fdjtechnicaltest.domain.usecase.GetFilteredTeamsByLeagueUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LeagueScreenViewModel @Inject constructor(
    private val getAllLeaguesUseCase: GetAllLeaguesUseCase,
    private val getFilteredTeamsByLeagueUseCase: GetFilteredTeamsByLeagueUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(LeagueScreenUiState())
    val uiState: StateFlow<LeagueScreenUiState> = _uiState.asStateFlow()

    init {
        loadAllLeagues()
    }

    fun onSearchQueryChange(query: String) {
        _uiState.update { currentState ->
            val filteredLeagues = if (query.isBlank()) {
                emptyList()
            } else {
                currentState.leagues.filter { league ->
                    league.name.contains(query, ignoreCase = true)
                }
            }

            currentState.copy(
                searchQuery = query,
                filteredLeagues = filteredLeagues,
                showResults = query.isNotEmpty()
            )
        }
    }

    fun onLeagueSelected(league: League) {
        val previousSelectedLeague = _uiState.value.selectedLeague

        _uiState.update { currentState ->
            currentState.copy(
                selectedLeague = league,
                searchQuery = league.name,
                showResults = false,
                filteredLeagues = emptyList()
            )
        }

        if (previousSelectedLeague == league) return
        loadTeamsForLeague(league.name)
    }

    fun onClearSearch() {
        _uiState.update { currentState ->
            currentState.copy(
                searchQuery = "",
                showResults = false,
                filteredLeagues = emptyList(),
                selectedLeague = null,
                teams = emptyList()
            )
        }
    }

    private fun loadAllLeagues() {
        viewModelScope.launch {
            _uiState.update { it.copy(leaguesError = null) }

            getAllLeaguesUseCase().collect { result ->
                result.fold(
                    onSuccess = { leagues ->
                        _uiState.update { currentState ->
                            currentState.copy(
                                leagues = leagues,
                                leaguesError = null
                            )
                        }
                    },
                    onFailure = { throwable ->
                        _uiState.update { currentState ->
                            currentState.copy(
                                leaguesError = throwable.message ?: "Unknown error occurred"
                            )
                        }
                    }
                )
            }
        }
    }

    private fun loadTeamsForLeague(leagueName: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoadingTeams = true, teamsError = null) }

            getFilteredTeamsByLeagueUseCase(GetFilteredTeamsByLeagueUseCase.Params(leagueName)).collect { result ->
                result.fold(
                    onSuccess = { teams ->
                        _uiState.update { currentState ->
                            currentState.copy(
                                teams = teams,
                                isLoadingTeams = false,
                                teamsError = null
                            )
                        }
                    },
                    onFailure = { throwable ->
                        _uiState.update { currentState ->
                            currentState.copy(
                                isLoadingTeams = false,
                                teamsError = throwable.message ?: "Failed to load teams"
                            )
                        }
                    }
                )
            }
        }
    }
}
