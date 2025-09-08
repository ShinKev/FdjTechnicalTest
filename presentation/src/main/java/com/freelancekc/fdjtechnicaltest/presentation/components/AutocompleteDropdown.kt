package com.freelancekc.fdjtechnicaltest.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.freelancekc.fdjtechnicaltest.domain.models.League
import com.freelancekc.fdjtechnicaltest.presentation.theme.FdjTechnicalTestTheme

@Composable
fun AutocompleteDropdown(
    suggestions: List<League>,
    onSuggestionClick: (League) -> Unit,
    modifier: Modifier = Modifier
) {
    if (suggestions.isNotEmpty()) {
        Card(
            modifier = modifier.fillMaxWidth(),
            shape = RoundedCornerShape(14.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(max = 200.dp)
                    .background(MaterialTheme.colorScheme.surface)
            ) {
                items(suggestions) { league ->
                    SuggestionItem(
                        league = league,
                        onSuggestionClick = onSuggestionClick
                    )
                    
                    if (league != suggestions.last()) {
                        HorizontalDivider(
                            color = MaterialTheme.colorScheme.outline.copy(alpha = 0.2f),
                            thickness = 0.5.dp
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun SuggestionItem(
    league: League,
    onSuggestionClick: (League) -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onSuggestionClick(league) }
            .padding(horizontal = 16.dp, vertical = 12.dp)
    ) {
        Text(
            text = league.name,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurface,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@Preview(showBackground = true)
@Composable
fun AutocompleteDropdownPreview() {
    val sampleLeagues = listOf(
        League("1", "Ligue 1"),
        League("2", "Premier League"),
        League("3", "La Liga"),
        League("4", "Serie A"),
        League("5", "Bundesliga")
    )
    
    FdjTechnicalTestTheme {
        Column {
            AutocompleteDropdown(
                suggestions = sampleLeagues,
                onSuggestionClick = {},
                modifier = Modifier.padding(16.dp)
            )
        }
    }
}

