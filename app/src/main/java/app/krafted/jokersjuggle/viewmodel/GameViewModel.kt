package app.krafted.jokersjuggle.viewmodel

import androidx.lifecycle.ViewModel
import app.krafted.jokersjuggle.game.GameSnapshot
import app.krafted.jokersjuggle.game.JokerEvent
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class GameViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(GameUiState())
    val uiState: StateFlow<GameUiState> = _uiState.asStateFlow()

    fun onSnapshot(s: GameSnapshot) {
        _uiState.value = _uiState.value.copy(
            score = s.score,
            lives = s.lives,
            airborneCount = s.airborneCount,
            multiplier = s.multiplier,
            elapsedSeconds = s.elapsedSeconds,
            nextObjectCountdown = s.nextObjectCountdown,
            audienceExcitement = s.excitement,
            isGoldStarActive = s.isMultiplierActive,
            goldStarSecondsLeft = s.multiplierSecondsLeft,
            screenAlpha = s.screenAlpha,
            controlsSwapped = s.controlsSwapped,
            isGameOver = s.isGameOver,
            bestScore = s.bestScore,
            maxObjectsReached = s.maxObjectsReached
        )
    }

    fun onJokerEvent(e: JokerEvent) {
        val (expression, quote) = quoteFor(e)
        _uiState.value = _uiState.value.copy(
            jokerExpression = expression,
            jokerQuote = quote
        )
    }

    fun resetState() {
        _uiState.value = GameUiState()
    }
}
