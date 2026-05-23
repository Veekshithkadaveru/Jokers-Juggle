package app.krafted.jokersjuggle.game

class AudienceExcitement {
    var value: Float = 0f
        private set

    fun onCatch() {
        value = (value + 3f).coerceAtMost(100f)
    }

    fun onDrop() {
        value = (value - 20f).coerceAtLeast(0f)
    }

    fun getBackgroundIndex(): Int {
        return when {
            value < 25f -> 0
            value < 50f -> 1
            value < 75f -> 2
            else -> 3
        }
    }
}
