package app.krafted.jokersjuggle.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import app.krafted.jokersjuggle.ui.theme.StageDark

@Composable
fun GameOverScreen(
    score: Int,
    timeSurvivedSeconds: Int,
    maxObjectsReached: Int,
    onReplayClick: () -> Unit,
    onHomeClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(StageDark),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "GAME OVER",
            color = Color(0xFFC91A1A),
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold,
            fontFamily = FontFamily.Serif
        )
        Spacer(modifier = Modifier.height(24.dp))

        Text(text = "Final Score: $score", color = Color.White, fontSize = 20.sp)
        Spacer(modifier = Modifier.height(8.dp))

        Text(text = "Time Survived: ${formatTime(timeSurvivedSeconds)}", color = Color.White, fontSize = 16.sp)
        Spacer(modifier = Modifier.height(8.dp))

        Text(text = "Max Objects: $maxObjectsReached", color = Color.White, fontSize = 16.sp)
        Spacer(modifier = Modifier.height(32.dp))

        Button(onClick = onReplayClick) {
            Text(text = "Replay")
        }
        Spacer(modifier = Modifier.height(12.dp))
        Button(onClick = onHomeClick) {
            Text(text = "Home")
        }
    }
}

private fun formatTime(seconds: Int): String {
    val mins = seconds / 60
    val secs = seconds % 60
    return "%02d:%02d".format(mins, secs)
}
