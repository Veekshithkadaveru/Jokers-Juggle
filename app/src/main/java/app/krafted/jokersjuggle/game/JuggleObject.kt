package app.krafted.jokersjuggle.game

data class JuggleObject(
    val id: Int,
    val type: ObjectType,
    var x: Float,
    var y: Float,
    var velocityX: Float,
    var velocityY: Float,
    var rotationAngle: Float = 0f,
    var isAirborne: Boolean = true,
    var wobblePhase: Float = 0f,
    var caughtByHand: Hand? = null
) {
    val radius: Float = objectRadius(type)
}
