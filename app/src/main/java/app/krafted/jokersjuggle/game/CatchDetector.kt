package app.krafted.jokersjuggle.game

import kotlin.math.abs

object CatchDetector {
    fun checkCatch(obj: JuggleObject, hand: Hand): Boolean {
        if (obj.velocityY < 0f) return false

        val dx = abs(obj.x - hand.x)
        val dy = abs(obj.y - hand.y)

        val halfW = hand.catchWidth / 2f
        val halfH = hand.catchHeight / 2f

        return dx < halfW + obj.radius * 0.5f &&
                dy < halfH + obj.radius * 0.5f
    }
}
