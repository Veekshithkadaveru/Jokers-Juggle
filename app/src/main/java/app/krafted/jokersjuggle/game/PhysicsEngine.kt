package app.krafted.jokersjuggle.game

import kotlin.math.sin
import kotlin.random.Random

object PhysicsEngine {
    const val GRAVITY: Float = 0.35f
    const val WOBBLE_AMPLITUDE: Float = 1.5f
    const val WOBBLE_SPEED: Float = 0.12f
    const val CHERRY_DRIFT: Float = 0.5f

    private val random = Random.Default

    fun update(obj: FallingObject, boardWidth: Float, speedMultiplier: Float = 1f) {
        when (obj.type) {
            ObjectType.GRAPES -> {
                obj.wobblePhase += WOBBLE_SPEED
                obj.x += sin(obj.wobblePhase) * WOBBLE_AMPLITUDE
            }

            ObjectType.CHERRIES -> {
                obj.velocityX += (random.nextFloat() * 2f - 1f) * CHERRY_DRIFT
            }

            ObjectType.ORANGE -> {
                obj.velocityX = 0f
            }

            ObjectType.GOLD_X, ObjectType.JOKER_HAT, ObjectType.GOLD_STAR, ObjectType.LUCKY_7 -> {}
        }

        obj.velocityY += GRAVITY * obj.type.gravityMultiplier

        obj.y += obj.velocityY * speedMultiplier
        obj.x += obj.velocityX

        val minX = obj.radius
        val maxX = boardWidth - obj.radius
        val clamped = obj.x.coerceIn(minX, maxX)
        if (clamped != obj.x) {
            obj.x = clamped
            obj.velocityX = 0f
        }
    }
}
