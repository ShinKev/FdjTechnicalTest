package com.freelancekc.fdjtechnicaltest.presentation.screens

import com.freelancekc.fdjtechnicaltest.domain.models.League
import com.freelancekc.fdjtechnicaltest.domain.models.Team

data class LeagueScreenUiState(
    val searchQuery: String = "",
    val leagues: List<League> = emptyList(),
    val filteredLeagues: List<League> = emptyList(),
    val selectedLeague: League? = null,
    val teams: List<Team> = emptyList(),
    val isLoadingTeams: Boolean = false,
    val leaguesError: String? = null,
    val teamsError: String? = null,
    val showResults: Boolean = false
)
