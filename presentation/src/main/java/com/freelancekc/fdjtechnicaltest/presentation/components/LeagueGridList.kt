package com.freelancekc.fdjtechnicaltest.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.freelancekc.fdjtechnicaltest.domain.models.Team
import com.freelancekc.fdjtechnicaltest.presentation.theme.FdjTechnicalTestTheme

@Composable
fun LeagueGridList(
    teams: List<Team>,
    modifier: Modifier = Modifier,
    isLoading: Boolean = false
) {
    Box(
        modifier = modifier
            .background(MaterialTheme.colorScheme.surface)
            .fillMaxSize()
    ) {
        when {
            isLoading -> {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center),
                    color = MaterialTheme.colorScheme.primary
                )
            }
            else -> {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    modifier = Modifier.fillMaxSize(),
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp),
                    contentPadding = PaddingValues(16.dp)
                ) {
                    items(teams) { team ->
                        TeamItem(
                            imageUrl = team.imageUrl,
                            teamName = team.name
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun LeagueGridListLoadingPreview() {
    FdjTechnicalTestTheme {
        LeagueGridList(
            teams = emptyList(),
            isLoading = true
        )
    }
}

@Preview(showBackground = true)
@Composable
fun LeagueGridListWithDataPreview() {
    FdjTechnicalTestTheme {
        LeagueGridList(
            teams = listOf(
                Team("1", "Paris Saint-Germain", null),
                Team("2", "Olympique de Marseille", null),
                Team("3", "AS Monaco", null),
                Team("4", "Olympique Lyonnais", null),
                Team("5", "FC Nantes", null),
                Team("6", "OGC Nice", null)
            )
        )
    }
}
