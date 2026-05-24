package app.krafted.jokersjuggle.game

import kotlin.math.abs

enum class HandState { IDLE, CATCH, THROW }

class Hand(val isLeft: Boolean) {
    var x: Float = 0f
    var targetX: Float = 0f
    var y: Float = 0f
    var prevX: Float = 0f
    var velocity: Float = 0f
    var currentSpeed: Float = 0f

    val catchWidth = 280f
    val catchHeight = 120f

    var scaleX: Float = 1f
    var scaleY: Float = 1f
    var offsetY: Float = 0f

    var state: HandState = HandState.IDLE
    private var stateTimerMs: Long = 0L
    var justReleased: Boolean = false

    fun update(deltaTime: Float) {
        justReleased = false
        prevX = x
        x = x + (targetX - x) * (HAND_LERP_SPEED * deltaTime).coerceIn(0f, 1f)
        velocity = x - prevX
        currentSpeed = if (deltaTime > 0f) abs(velocity) / deltaTime else 0f

        if (deltaTime > 0f) {
            scaleX += (1f - scaleX) * 12f * deltaTime
            scaleY += (1f - scaleY) * 12f * deltaTime
            offsetY += (0f - offsetY) * 12f * deltaTime
        }

        if (stateTimerMs > 0L) {
            stateTimerMs -= (deltaTime * 1000f).toLong()
            if (stateTimerMs <= 0L) {
                when (state) {
                    HandState.CATCH -> {
                        state = HandState.THROW
                        stateTimerMs = 150L
                        scaleY = 1.4f
                        scaleX = 0.8f
                        offsetY = -50f
                    }
                    HandState.THROW -> {
                        state = HandState.IDLE
                        stateTimerMs = 0L
                        justReleased = true
                    }
                    else -> {}
                }
            }
        }
    }

    fun triggerCatchAnimation(isBomb: Boolean = false) {
        state = HandState.CATCH
        stateTimerMs = 120L

        if (isBomb) {
            scaleY = 0.5f
            scaleX = 1.4f
            offsetY = 45f
        } else {
            scaleY = 0.7f
            scaleX = 1.2f
            offsetY = 25f
        }
    }

    fun reset() {
        state = HandState.IDLE
        stateTimerMs = 0L
        scaleX = 1f
        scaleY = 1f
        offsetY = 0f
        justReleased = false
        velocity = 0f
        currentSpeed = 0f
    }

    companion object {
        const val HAND_LERP_SPEED = 12f
    }
}
