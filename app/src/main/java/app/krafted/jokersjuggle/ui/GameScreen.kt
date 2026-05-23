package app.krafted.jokersjuggle.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import app.krafted.jokersjuggle.game.JuggleGameView

@Composable
fun GameScreen(
    act: Int,
    onActComplete: (Int, Int) -> Unit,
    onGameOver: () -> Unit
) {
    AndroidView(
        factory = { ctx ->
            JuggleGameView(ctx)
        },
        update = { view ->
            view.act = act
            view.onActComplete = onActComplete
            view.onGameOver = onGameOver
        },
        modifier = Modifier.fillMaxSize()
    )
}
