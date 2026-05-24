package app.krafted.jokersjuggle.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import app.krafted.jokersjuggle.data.db.AppDatabase
import app.krafted.jokersjuggle.data.db.ScoreRecord
import app.krafted.jokersjuggle.game.GameSnapshot
import app.krafted.jokersjuggle.game.JokerEvent
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class GameViewModel(application: Application) : AndroidViewModel(application) {

    private val _uiState = MutableStateFlow(GameUiState())
    val uiState: StateFlow<GameUiState> = _uiState.asStateFlow()

    private val database = AppDatabase.getDatabase(application)
    private val juggleDao = database.juggleDao()

    init {
        loadBestScore()
    }

    private fun loadBestScore() {
        viewModelScope.launch {
            val best = juggleDao.getBestScore() ?: 0
            val maxObjects = juggleDao.getMaxObjectsReached() ?: 0
            _uiState.value = _uiState.value.copy(
                bestScore = best,
                maxObjectsReached = maxObjects
            )
        }
    }

    fun saveScore(score: Int, time: Int, maxObjects: Int, playerName: String = "") {
        viewModelScope.launch {
            val record = ScoreRecord(
                playerName = playerName,
                score = score,
                timeSurvivedSeconds = time,
                maxObjectsReached = maxObjects
            )
            val currentBest = juggleDao.getBestScore() ?: 0
            juggleDao.insertScore(record)
            if (score > currentBest && currentBest > 0) {
                onJokerEvent(JokerEvent.NEW_HIGH_SCORE)
            }
            loadBestScore()
        }
    }

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
            maxObjectsReached = maxOf(_uiState.value.maxObjectsReached, s.maxObjectsReached)
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
        val currentBest = _uiState.value.bestScore
        _uiState.value = GameUiState(bestScore = currentBest)
    }
}
