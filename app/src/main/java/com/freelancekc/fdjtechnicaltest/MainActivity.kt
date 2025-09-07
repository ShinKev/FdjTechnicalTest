package com.freelancekc.fdjtechnicaltest

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.freelancekc.fdjtechnicaltest.presentation.screens.LeagueScreen
import com.freelancekc.fdjtechnicaltest.presentation.theme.FdjTechnicalTestTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            FdjTechnicalTestTheme {
                LeagueScreen()
            }
        }
    }
}
