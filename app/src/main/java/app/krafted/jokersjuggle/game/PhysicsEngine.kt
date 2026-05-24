package app.krafted.jokersjuggle.game

import kotlin.math.abs
import kotlin.math.sin
import kotlin.random.Random

object PhysicsEngine {
    const val GRAVITY = 1200f

    fun update(obj: JuggleObject, boardWidth: Float, deltaTime: Float, gravityMultiplier: Float = 1f) {
        val effectiveGravity = GRAVITY * gravityMultiplier

        obj.velocityY += effectiveGravity * deltaTime

        val gravityMod = when (obj.type) {
            ObjectType.CHERRIES -> 0.6f
            ObjectType.ORANGE -> 1.4f
            ObjectType.GOLD_X -> 1.6f
            else -> 1.0f
        }
        obj.velocityY += effectiveGravity * (gravityMod - 1f) * deltaTime

        obj.x += obj.velocityX * deltaTime
        obj.y += obj.velocityY * deltaTime

        obj.rotationAngle += when (obj.type) {
            ObjectType.JOKER_HAT -> 180f * deltaTime
            ObjectType.LUCKY_7 -> 90f * deltaTime
            else -> 30f * deltaTime
        }

        if (obj.type == ObjectType.GRAPES) {
            obj.wobblePhase += deltaTime * 3f
            obj.x += sin(obj.wobblePhase) * 1.2f
        }

        if (obj.type == ObjectType.LUCKY_7 && obj.velocityY < 0 &&
            Random.nextFloat() < 0.01f
        ) {
            obj.velocityX *= -1f
        }

        val radius = obj.radius
        if (obj.x < radius) {
            obj.x = radius
            obj.velocityX = abs(obj.velocityX)
        }
        if (obj.x > boardWidth - radius) {
            obj.x = boardWidth - radius
            obj.velocityX = -abs(obj.velocityX)
        }
    }
}
