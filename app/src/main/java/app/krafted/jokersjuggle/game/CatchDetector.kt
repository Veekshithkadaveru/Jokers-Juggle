package app.krafted.jokersjuggle.game

import kotlin.math.abs

object CatchDetector {
    fun checkCatch(obj: FallingObject, hand: Hand): Boolean {
        val dx = abs(obj.x - hand.x)
        val dy = abs(obj.y - hand.y)
        return dx < 65f + obj.radius * 0.5f &&
                dy < 36f + obj.radius * 0.5f &&
                obj.y >= hand.y - 36f
    }
}
