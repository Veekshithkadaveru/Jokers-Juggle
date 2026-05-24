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

    fun setAct(act: Int) {
        _uiState.value = _uiState.value.copy(currentAct = act)
    }

    fun onSnapshot(s: GameSnapshot) {
        _uiState.value = _uiState.value.copy(
            score = s.score,
            lives = s.lives,
            currentAct = s.act,
            timeRemainingMs = s.timeRemainingMs,
            comboStreak = s.comboStreak,
            comboMultiplier = s.comboMultiplier,
            audienceExcitement = s.excitement,
            isMultiplierActive = s.isMultiplierActive,
            multiplierSecondsLeft = s.multiplierSecondsLeft,
            screenAlpha = s.screenAlpha,
            controlsSwapped = s.controlsSwapped
        )
    }

    fun onJokerEvent(e: JokerEvent) {
        val (expression, quote) = quoteFor(e)
        _uiState.value = _uiState.value.copy(
            jokerExpression = expression,
            jokerQuote = quote
        )
    }
}
