package app.krafted.jokersjuggle.viewmodel

enum class JokerExpression {
    NEUTRAL, AMUSED, IMPRESSED, GLEEFUL, SINISTER, LAUGHING,
    UNHINGED, ECSTATIC, THEATRICAL, TRIUMPHANT, GENUINE
}

data class GameUiState(
    val score: Int = 0,
    val lives: Int = 3,
    val currentAct: Int = 1,
    val timeRemainingMs: Long = 60_000L,
    val comboStreak: Int = 0,
    val comboMultiplier: Int = 1,
    val audienceExcitement: Float = 0f,
    val isMultiplierActive: Boolean = false,
    val multiplierSecondsLeft: Int = 0,
    val jokerExpression: JokerExpression = JokerExpression.THEATRICAL,
    val jokerQuote: String = "",
    val controlsSwapped: Boolean = false,
    val screenAlpha: Float = 1f
)
