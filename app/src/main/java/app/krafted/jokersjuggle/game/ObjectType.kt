package app.krafted.jokersjuggle.game

enum class ObjectType { GRAPES, CHERRIES, ORANGE, JOKER_HAT, GOLD_STAR, LUCKY_7, GOLD_X }

val ObjectType.gravityMultiplier: Float
    get() = when (this) {
        ObjectType.ORANGE -> 1.5f
        ObjectType.GOLD_X -> 1.8f
        else -> 1.0f
    }

val ObjectType.points: Int
    get() = when (this) {
        ObjectType.GRAPES -> 10
        ObjectType.CHERRIES -> 15
        ObjectType.ORANGE -> 20
        ObjectType.JOKER_HAT -> 30
        ObjectType.GOLD_STAR -> 50
        ObjectType.LUCKY_7 -> 100
        ObjectType.GOLD_X -> 0
    }

fun objectRadius(type: ObjectType): Float = when (type) {
    ObjectType.ORANGE -> 46f
    ObjectType.GOLD_X -> 44f
    ObjectType.JOKER_HAT -> 42f
    ObjectType.GRAPES -> 40f
    ObjectType.LUCKY_7 -> 40f
    ObjectType.GOLD_STAR -> 38f
    ObjectType.CHERRIES -> 34f
}

fun baseFallSpeed(type: ObjectType, act: Int): Float {
    val base = when (type) {
        ObjectType.ORANGE, ObjectType.GOLD_X -> 5.5f
        ObjectType.CHERRIES, ObjectType.GOLD_STAR -> 2.5f
        else -> 3.5f
    }
    val actFactor = 1f + (act.coerceIn(1, 3) - 1) * 0.25f
    return base * actFactor
}
