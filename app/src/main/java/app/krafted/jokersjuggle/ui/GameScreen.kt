package app.krafted.jokersjuggle.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
    act: Int,
    onActComplete: (Int, Int) -> Unit,
    onGameOver: () -> Unit,
    vm: GameViewModel = viewModel()
) {
    val ui by vm.uiState.collectAsState()
    LaunchedEffect(act) { vm.setAct(act) }
    Box(Modifier.fillMaxSize()) {
        AndroidView(
            factory = { ctx -> JuggleGameView(ctx) },
            update = { v ->
                v.act = act
                v.onActComplete = onActComplete
                v.onGameOver = onGameOver
                v.onStateSnapshot = { vm.onSnapshot(it) }
                v.onJokerEvent = { vm.onJokerEvent(it) }
            },
            modifier = Modifier.fillMaxSize()
        )
        HudOverlay(ui, Modifier.align(Alignment.TopCenter))
        JokerReactionOverlay(ui, Modifier.fillMaxSize())
    }
}
