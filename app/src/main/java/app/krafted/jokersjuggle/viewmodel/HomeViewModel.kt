package app.krafted.jokersjuggle.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import app.krafted.jokersjuggle.data.db.AppDatabase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class HomeViewModel(application: Application) : AndroidViewModel(application) {
    private val _bestScore = MutableStateFlow(0)
    val bestScore: StateFlow<Int> = _bestScore.asStateFlow()

    private val _maxObjects = MutableStateFlow(0)
    val maxObjects: StateFlow<Int> = _maxObjects.asStateFlow()

    private val _longestTime = MutableStateFlow(0)
    val longestTime: StateFlow<Int> = _longestTime.asStateFlow()

    private val juggleDao = AppDatabase.getDatabase(application).juggleDao()

    init {
        loadBestScore()
    }

    fun loadBestScore() {
        viewModelScope.launch {
            _bestScore.value = juggleDao.getBestScore() ?: 0
            _maxObjects.value = juggleDao.getMaxObjectsReached() ?: 0
            _longestTime.value = juggleDao.getLongestSurvivalTime() ?: 0
        }
    }
}
