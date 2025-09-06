package com.freelancekc.fdjtechnicaltest.presentation.components

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
import com.freelancekc.fdjtechnicaltest.domain.models.SoccerClub
import com.freelancekc.fdjtechnicaltest.presentation.theme.FdjTechnicalTestTheme

@Composable
fun LeagueGridList(
    clubs: List<SoccerClub>,
    modifier: Modifier = Modifier,
    isLoading: Boolean = false
) {
    Box(
        modifier = modifier.fillMaxSize()
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
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    contentPadding = PaddingValues(16.dp)
                ) {
                    items(clubs) { club ->
                        ClubItem(
                            imageUrl = club.imageUrl,
                            clubName = club.name
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
            clubs = emptyList(),
            isLoading = true
        )
    }
}

@Preview(showBackground = true)
@Composable
fun LeagueGridListWithDataPreview() {
    FdjTechnicalTestTheme {
        LeagueGridList(
            clubs = listOf(
                SoccerClub("1", "Paris Saint-Germain", null),
                SoccerClub("2", "Olympique de Marseille", null),
                SoccerClub("3", "AS Monaco", null),
                SoccerClub("4", "Olympique Lyonnais", null),
                SoccerClub("5", "FC Nantes", null),
                SoccerClub("6", "OGC Nice", null)
            )
        )
    }
}
