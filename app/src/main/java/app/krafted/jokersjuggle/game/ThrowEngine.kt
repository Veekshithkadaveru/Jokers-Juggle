package app.krafted.jokersjuggle.game

import kotlin.math.abs

object ThrowEngine {
    const val MIN_THROW_VELOCITY = 0f
    const val MAX_THROW_VELOCITY = 1500f
    const val PERFECT_CATCH_RADIUS = 30f

    fun calculateThrow(obj: JuggleObject, hand: Hand, boardWidth: Float): Boolean {
        val handSpeed = hand.currentSpeed
        val throwStrength = (handSpeed * 0.6f)
            .coerceIn(MIN_THROW_VELOCITY, MAX_THROW_VELOCITY)

        val baseThrowY = when (obj.type) {
            ObjectType.CHERRIES -> -1100f
            ObjectType.ORANGE -> -750f
            ObjectType.GOLD_X -> -900f
            else -> -950f
        }

        obj.velocityY = baseThrowY - (throwStrength * 0.3f)

        val centreX = boardWidth / 2f
        obj.velocityX = (centreX - obj.x) * 0.3f + (hand.velocity * 0.2f)

        obj.rotationAngle = 0f

        val catchOffset = abs(obj.x - hand.x)
        if (catchOffset < PERFECT_CATCH_RADIUS) {
            obj.velocityX *= 0.5f
            return true
        }
        return false
    }
}

