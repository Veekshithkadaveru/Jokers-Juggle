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
import androidx.compose.ui.unit.dp
import app.krafted.jokersjuggle.ui.theme.StageDark

@Composable
fun ActCompleteScreen(
    act: Int,
    score: Int,
    onNextActClick: () -> Unit,
    onHomeClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(StageDark),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Act $act Complete", color = Color.White)
        Text(text = "Score: $score", color = Color.LightGray)
        Spacer(modifier = Modifier.height(24.dp))
        Button(onClick = onNextActClick) {
            Text(text = if (act < 3) "Next Act" else "Grand Finale")
        }
        Spacer(modifier = Modifier.height(12.dp))
        Button(onClick = onHomeClick) {
            Text(text = "Home")
        }
    }
}
