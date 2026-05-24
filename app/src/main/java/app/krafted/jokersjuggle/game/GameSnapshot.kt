package app.krafted.jokersjuggle.game

data class GameSnapshot(
    val score: Int,
    val lives: Int,
    val airborneCount: Int,
    val multiplier: Int,
    val elapsedSeconds: Int,
    val nextObjectCountdown: Int,
    val excitement: Float,
    val isMultiplierActive: Boolean,
    val multiplierSecondsLeft: Int,
    val screenAlpha: Float = 1f,
    val controlsSwapped: Boolean = false,
    val isGameOver: Boolean = false,
    val bestScore: Int = 0,
    val maxObjectsReached: Int = 0
)
