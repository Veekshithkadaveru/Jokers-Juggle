package app.krafted.jokersjuggle.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import app.krafted.jokersjuggle.data.db.AppDatabase
import app.krafted.jokersjuggle.data.db.ScoreRecord
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class LeaderboardViewModel(application: Application) : AndroidViewModel(application) {
    private val _scores = MutableStateFlow<List<ScoreRecord>>(emptyList())
    val scores: StateFlow<List<ScoreRecord>> = _scores.asStateFlow()

    private val juggleDao = AppDatabase.getDatabase(application).juggleDao()

    init {
        loadScores()
    }

    fun loadScores() {
        viewModelScope.launch {
            _scores.value = juggleDao.getTopScores()
        }
    }
}
