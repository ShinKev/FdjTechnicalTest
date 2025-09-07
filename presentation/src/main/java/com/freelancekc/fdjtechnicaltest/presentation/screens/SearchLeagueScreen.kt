package com.freelancekc.fdjtechnicaltest.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.freelancekc.fdjtechnicaltest.domain.models.SoccerClub
import com.freelancekc.fdjtechnicaltest.presentation.components.CancelButton
import com.freelancekc.fdjtechnicaltest.presentation.components.LeagueGridList
import com.freelancekc.fdjtechnicaltest.presentation.components.SearchTextField
import com.freelancekc.fdjtechnicaltest.presentation.theme.FdjTechnicalTestTheme

@Composable
fun LeagueScreen(
    modifier: Modifier = Modifier
) {
    var searchQuery by remember { mutableStateOf("") }
    var showResults by remember { mutableStateOf(false) }
    
    // Sample data for demonstration
    val sampleClubs = listOf(
        SoccerClub("1", "Paris Saint-Germain", null),
        SoccerClub("2", "Olympique de Marseille", null),
        SoccerClub("3", "AS Monaco", null),
        SoccerClub("4", "Olympique Lyonnais", null),
        SoccerClub("5", "FC Nantes", null),
        SoccerClub("6", "OGC Nice", null),
        SoccerClub("7", "Stade Rennais", null),
        SoccerClub("8", "RC Strasbourg Alsace", null),
        SoccerClub("9", "AS Saint-Étienne", null),
        SoccerClub("10", "Toulouse FC", null)
    )

    Scaffold(
        modifier = modifier.fillMaxSize()
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .background(color = Color(0xFFD9D9D9))
                    .padding(horizontal = 16.dp, vertical = 16.dp)
                    .fillMaxWidth()
            ) {
                SearchTextField(
                    value = searchQuery,
                    onValueChange = { 
                        searchQuery = it
                        showResults = it.isNotEmpty()
                    },
                    placeholder = "Search by league",
                    modifier = Modifier.weight(1f),
                    onClearClick = {
                        showResults = false
                    }
                )
                
                if (searchQuery.isNotEmpty()) {
                    Spacer(modifier = Modifier.width(8.dp))
                    CancelButton(
                        onClick = {
                            searchQuery = ""
                            showResults = false
                        }
                    )
                }
            }

            LeagueGridList(
                clubs = if (showResults) sampleClubs else emptyList(),
                modifier = Modifier.weight(1f),
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun LeagueScreenPreview() {
    FdjTechnicalTestTheme {
        LeagueScreen()
    }
}
