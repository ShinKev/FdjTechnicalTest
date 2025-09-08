package com.freelancekc.fdjtechnicaltest.presentation.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.freelancekc.fdjtechnicaltest.domain.models.League
import com.freelancekc.fdjtechnicaltest.domain.models.Team
import com.freelancekc.fdjtechnicaltest.presentation.actions.LeagueScreenActions
import com.freelancekc.fdjtechnicaltest.presentation.components.AutocompleteDropdown
import com.freelancekc.fdjtechnicaltest.presentation.components.CancelButton
import com.freelancekc.fdjtechnicaltest.presentation.components.LeagueGridList
import com.freelancekc.fdjtechnicaltest.presentation.components.SearchTextField
import com.freelancekc.fdjtechnicaltest.presentation.theme.FdjTechnicalTestTheme

private typealias LeagueScreenActioner = (LeagueScreenActions) -> Unit

@Composable
fun LeagueScreenStateful(
    viewModel: LeagueScreenViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(uiState.leaguesError) {
        uiState.leaguesError?.let { error ->
            snackbarHostState.showSnackbar(error)
        }
    }

    LaunchedEffect(uiState.teamsError) {
        uiState.teamsError?.let { error ->
            snackbarHostState.showSnackbar(error)
        }
    }

    val actioner: LeagueScreenActioner = {
        when (it) {
            LeagueScreenActions.OnSearchBarClear -> viewModel.onClearSearch()
            is LeagueScreenActions.OnSearchBarValueChange -> viewModel.onSearchQueryChange(it.query)
            is LeagueScreenActions.OnSuggestionClick -> viewModel.onLeagueSelected(it.league)
        }
    }

    LeagueScreen(
        uiState = uiState,
        snackbarHostState = snackbarHostState,
        actioner = actioner
    )
}

@Composable
fun LeagueScreen(
    uiState: LeagueScreenUiState,
    snackbarHostState: SnackbarHostState,
    actioner: LeagueScreenActioner,
    modifier: Modifier = Modifier
) {
    val focusManager = LocalFocusManager.current

    Scaffold(
        containerColor = MaterialTheme.colorScheme.surfaceContainer,
        modifier = modifier.fillMaxSize(),
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .padding(vertical = 12.dp, horizontal = 16.dp)
                        .fillMaxWidth()
                ) {
                    SearchTextField(
                        value = uiState.searchQuery,
                        onValueChange = {
                            actioner.invoke(LeagueScreenActions.OnSearchBarValueChange(it))
                        },
                        placeholder = "Search by league",
                        modifier = Modifier.weight(1f),
                        onClearClick = { actioner.invoke(LeagueScreenActions.OnSearchBarClear) }
                    )

                    if (uiState.searchQuery.isNotEmpty()) {
                        Spacer(modifier = Modifier.width(8.dp))
                        CancelButton(
                            onClick = { actioner.invoke(LeagueScreenActions.OnSearchBarClear) }
                        )
                    }
                }

                LeagueGridList(
                    clubs = uiState.teams,
                    isLoading = uiState.isLoadingTeams,
                    modifier = Modifier.weight(1f)
                )
            }

            if (uiState.showResults && uiState.filteredLeagues.isNotEmpty()) {
                AutocompleteDropdown(
                    suggestions = uiState.filteredLeagues,
                    onSuggestionClick = {
                        actioner.invoke(LeagueScreenActions.OnSuggestionClick(it))
                        focusManager.clearFocus()
                    },
                    modifier = Modifier.padding(top = 78.dp, start = 16.dp, end = 16.dp)
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun LeagueScreenInitialStatePreview() {
    FdjTechnicalTestTheme {
        val uiState = LeagueScreenUiState(
            searchQuery = "",
            leagues = emptyList(),
            filteredLeagues = emptyList(),
            selectedLeague = null,
            teams = emptyList(),
            isLoadingTeams = false,
            leaguesError = null,
            teamsError = null,
            showResults = false
        )

        LeagueScreen(
            uiState = uiState,
            snackbarHostState = remember { SnackbarHostState() },
            actioner = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
fun LeagueScreenWithDropdownPreview() {
    FdjTechnicalTestTheme {
        val uiState = LeagueScreenUiState(
            searchQuery = "L",
            leagues = listOf(
                League("Premier League", "Premier League"),
                League("Ligue 1", "Ligue 1"),
                League("Bundesliga", "Bundesliga")
            ),
            filteredLeagues = listOf(
                League("Premier League", "Premier League"),
                League("Ligue 1", "Ligue 1"),
                League("Bundesliga", "Bundesliga")
            ),
            selectedLeague = null,
            teams = emptyList(),
            isLoadingTeams = false,
            leaguesError = null,
            teamsError = null,
            showResults = true
        )

        LeagueScreen(
            uiState = uiState,
            snackbarHostState = remember { SnackbarHostState() },
            actioner = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
fun LeagueScreenWithDataPreview() {
    FdjTechnicalTestTheme {
        val uiState = LeagueScreenUiState(
            searchQuery = "Premier League",
            leagues = listOf(
                League("Premier League", "Premier League"),
                League("Ligue 1", "Ligue 1"),
                League("Bundesliga", "Bundesliga")
            ),
            filteredLeagues = emptyList(),
            selectedLeague = League("Premier League", "Premier League"),
            teams = listOf(
                Team(
                    id = "1",
                    name = "Arsenal",
                    imageUrl = ""
                ),
                Team(
                    id = "2",
                    name = "Manchester City",
                    imageUrl = ""
                )
            ),
            isLoadingTeams = false,
            leaguesError = null,
            teamsError = null,
            showResults = false
        )

        LeagueScreen(
            uiState = uiState,
            snackbarHostState = remember { SnackbarHostState() },
            actioner = {}
        )
    }
}
