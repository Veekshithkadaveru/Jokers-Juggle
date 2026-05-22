package app.krafted.jokersjuggle.game

data class FallingObject(
    val type: ObjectType,
    var x: Float,
    var y: Float,
    var velocityX: Float = 0f,
    var velocityY: Float = 0f,
    var radius: Float = objectRadius(type),
    var isActive: Boolean = true,
    var wobblePhase: Float = 0f
)

fun spawnAt(type: ObjectType, x: Float, act: Int): FallingObject =
    FallingObject(
        type = type,
        x = x,
        y = -50f,
        velocityX = 0f,
        velocityY = baseFallSpeed(type, act),
        isActive = true,
        wobblePhase = 0f
    )
