package app.krafted.jokersjuggle.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.viewmodel.compose.viewModel
import app.krafted.jokersjuggle.game.JuggleGameView
import app.krafted.jokersjuggle.ui.components.HudOverlay
import app.krafted.jokersjuggle.ui.components.JokerReactionOverlay
import app.krafted.jokersjuggle.viewmodel.GameViewModel

@Composable
fun GameScreen(
    onGameOver: (score: Int, timeSurvivedSeconds: Int, maxObjectsReached: Int) -> Unit,
    vm: GameViewModel = viewModel()
) {
    val ui by vm.uiState.collectAsState()
    Box(Modifier.fillMaxSize()) {
        AndroidView(
            factory = { ctx ->
                JuggleGameView(ctx).apply {
                    this.onGameOver = { score, elapsedSeconds, maxObjects ->
                        onGameOver(score, elapsedSeconds, maxObjects)
                    }
                    this.onStateSnapshot = { vm.onSnapshot(it) }
                    this.onJokerEvent = { vm.onJokerEvent(it) }
                }
            },
            update = { _ -> },
            modifier = Modifier.fillMaxSize()
        )
        HudOverlay(ui, Modifier.align(Alignment.TopCenter))
        JokerReactionOverlay(ui, Modifier.fillMaxSize())
    }
}
