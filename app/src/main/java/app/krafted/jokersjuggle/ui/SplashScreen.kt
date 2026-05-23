package app.krafted.jokersjuggle.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import app.krafted.jokersjuggle.ui.theme.StageDark
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(onNavigateToHome: () -> Unit) {
    LaunchedEffect(Unit) {
        delay(3000)
        onNavigateToHome()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(StageDark)
            .clickable { onNavigateToHome() },
        contentAlignment = Alignment.Center
    ) {
        Text(text = "Joker's Juggle\nTap to skip", color = Color.White)
    }
}
