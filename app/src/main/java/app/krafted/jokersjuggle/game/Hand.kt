package app.krafted.jokersjuggle.game

class Hand(val isLeft: Boolean) {
    var x: Float = 0f
    var y: Float = 0f
    var targetX: Float = 0f
    var prevX: Float = 0f

    fun update(deltaSeconds: Float) {
        x += (targetX - x) * (HAND_SPEED * deltaSeconds).coerceIn(0f, 1f)
    }

    companion object {
        const val HAND_SPEED: Float = 12f
    }
}
