package app.krafted.jokersjuggle.game

import kotlin.random.Random

class JuggleSpawner(private val boardWidth: Float, private val boardHeight: Float) {

    private var nextId = 1
    private var nextSpawnTime = 15_000L

    fun spawnWeights(airborneCount: Int): Map<ObjectType, Int> = mapOf(
        ObjectType.GRAPES to 25,
        ObjectType.CHERRIES to 20,
        ObjectType.ORANGE to 18,
        ObjectType.JOKER_HAT to 12,
        ObjectType.GOLD_STAR to 10,
        ObjectType.LUCKY_7 to 8,
        ObjectType.GOLD_X to if (airborneCount >= 4) 7 else 0
    )

    fun launchObject(type: ObjectType): JuggleObject {
        val startX = Random.nextFloat() * (boardWidth * 0.6f) + boardWidth * 0.2f
        return JuggleObject(
            id = nextId++,
            type = type,
            x = startX,
            y = boardHeight * 0.85f,
            velocityX = Random.nextFloat() * 200f - 100f,
            velocityY = initialLaunchVelocity(type)
        )
    }

    private fun initialLaunchVelocity(type: ObjectType): Float = when (type) {
        ObjectType.CHERRIES -> -1500f
        ObjectType.ORANGE -> -1000f
        ObjectType.GOLD_X -> -1200f
        else -> -1300f
    }

    fun tick(elapsedMs: Long, airborneCount: Int): ObjectType? {
        if (airborneCount >= 7) return null
        if (elapsedMs >= nextSpawnTime) {
            nextSpawnTime += 15_000L
            return weightedRandom(spawnWeights(airborneCount))
        }
        return null
    }

    private fun weightedRandom(weights: Map<ObjectType, Int>): ObjectType {
        val total = weights.values.sum()
        if (total <= 0) return ObjectType.GRAPES
        var rolled = Random.nextInt(total)
        for ((type, weight) in weights) {
            if (rolled < weight) return type
            rolled -= weight
        }
        return ObjectType.GRAPES
    }

    fun reset() {
        nextSpawnTime = 15_000L
    }
}
