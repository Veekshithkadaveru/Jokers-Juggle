package app.krafted.jokersjuggle.game

class AudienceExcitement {
    var value: Float = 0f

    fun update(airborneCount: Int, dropped: Boolean, deltaTime: Float) {
        if (dropped) {
            value -= 20f
        } else {
            value += airborneCount * 0.5f * deltaTime
        }
        value = value.coerceIn(0f, 100f)
    }

    fun getBackgroundIndex(): Int {
        return when {
            value < 25f -> 0
            value < 50f -> 1
            value < 75f -> 2
            else -> 3
        }
    }

    fun reset() {
        value = 0f
    }
}
