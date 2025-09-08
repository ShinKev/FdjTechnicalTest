package com.freelancekc.fdjtechnicaltest.presentation.actions

import com.freelancekc.fdjtechnicaltest.domain.models.League

sealed interface LeagueScreenActions {
    data class OnSearchBarValueChange(val query: String): LeagueScreenActions
    data class OnSuggestionClick(val league: League): LeagueScreenActions
    object OnSearchBarClear: LeagueScreenActions
}
