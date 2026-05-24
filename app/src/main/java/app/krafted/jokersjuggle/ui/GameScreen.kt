package app.krafted.jokersjuggle.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.viewmodel.compose.viewModel
import app.krafted.jokersjuggle.game.JuggleGameView
import app.krafted.jokersjuggle.ui.components.*
import app.krafted.jokersjuggle.viewmodel.GameViewModel

@Composable
fun GameScreen(
    onGameOver: (score: Int, timeSurvivedSeconds: Int, maxObjectsReached: Int) -> Unit,
    vm: GameViewModel = viewModel()
) {
    val ui by vm.uiState.collectAsState()
    var curtainOpen by remember { mutableStateOf(0f) }
    LaunchedEffect(Unit) {
        curtainOpen = 1f
    }

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
        CurtainDrapes(isOpen = curtainOpen)
    }
}
