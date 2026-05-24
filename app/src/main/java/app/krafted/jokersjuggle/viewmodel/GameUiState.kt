package app.krafted.jokersjuggle.viewmodel

enum class JokerExpression {
    NEUTRAL, AMUSED, IMPRESSED, GLEEFUL, SINISTER, LAUGHING,
    UNHINGED, ECSTATIC, THEATRICAL, TRIUMPHANT, GENUINE,
    SURPRISED, TAUNTING, RESPECTFUL
}

data class GameUiState(
    val score: Int = 0,
    val lives: Int = 3,
    val airborneCount: Int = 2,
    val multiplier: Int = 1,
    val elapsedSeconds: Int = 0,
    val nextObjectCountdown: Int = 15,
    val audienceExcitement: Float = 0f,
    val isGoldStarActive: Boolean = false,
    val goldStarSecondsLeft: Int = 0,
    val jokerExpression: JokerExpression = JokerExpression.NEUTRAL,
    val jokerQuote: String = "",
    val isGameOver: Boolean = false,
    val isNewBestScore: Boolean = false,
    val controlsSwapped: Boolean = false,
    val screenAlpha: Float = 1f,
    val bestScore: Int = 0,
    val maxObjectsReached: Int = 0
)
