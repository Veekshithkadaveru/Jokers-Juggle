package app.krafted.jokersjuggle.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import app.krafted.jokersjuggle.ui.theme.StageDark

@Composable
fun LeaderboardScreen(onBackClick: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(StageDark),
        contentAlignment = Alignment.Center
    ) {
        Text(text = "Leaderboard Screen", color = Color.White)
    }
}
