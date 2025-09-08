package com.freelancekc.fdjtechnicaltest.presentation.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.freelancekc.fdjtechnicaltest.presentation.theme.FdjTechnicalTestTheme

@Composable
fun CancelButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    text: String = "Cancel"
) {
    Box(
        modifier = modifier
            .padding(horizontal = 4.dp, vertical = 8.dp)
            .clickable(onClick = onClick)
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.primary,
        )
    }
}

@Preview(showBackground = true)
@Composable
fun CancelButtonPreview() {
    FdjTechnicalTestTheme {
        CancelButton(
            onClick = {}
        )
    }
}
