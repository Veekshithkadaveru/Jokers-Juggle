package app.krafted.jokersjuggle.game

data class GameSnapshot(
    val score: Int,
    val lives: Int,
    val act: Int,
    val timeRemainingMs: Long,
    val comboStreak: Int,
    val comboMultiplier: Int,
    val excitement: Float,
    val isMultiplierActive: Boolean,
    val multiplierSecondsLeft: Int,
    val screenAlpha: Float = 1f,
    val controlsSwapped: Boolean = false
)
