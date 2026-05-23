package app.krafted.jokersjuggle.game

import kotlin.random.Random

object JuggleSpawner {

    fun pickObjectType(act: Int): ObjectType {
        val rolled = Random.nextInt(100)
        return when (act) {
            2 -> when {
                rolled < 22 -> ObjectType.GRAPES
                rolled < 40 -> ObjectType.CHERRIES
                rolled < 58 -> ObjectType.ORANGE
                rolled < 70 -> ObjectType.JOKER_HAT
                rolled < 80 -> ObjectType.GOLD_STAR
                rolled < 88 -> ObjectType.LUCKY_7
                else -> ObjectType.GOLD_X
            }

            3 -> when {
                rolled < 18 -> ObjectType.GRAPES
                rolled < 33 -> ObjectType.CHERRIES
                rolled < 48 -> ObjectType.ORANGE
                rolled < 60 -> ObjectType.JOKER_HAT
                rolled < 68 -> ObjectType.GOLD_STAR
                rolled < 75 -> ObjectType.LUCKY_7
                else -> ObjectType.GOLD_X
            }

            else -> when {
                rolled < 30 -> ObjectType.GRAPES
                rolled < 55 -> ObjectType.CHERRIES
                rolled < 75 -> ObjectType.ORANGE
                rolled < 85 -> ObjectType.JOKER_HAT
                rolled < 95 -> ObjectType.GOLD_STAR
                rolled < 100 -> ObjectType.LUCKY_7
                else -> ObjectType.GOLD_X
            }
        }
    }

    fun getSpawnInterval(excitement: Float): Long {
        return when {
            excitement < 25f -> 1800L
            excitement < 50f -> 1400L
            excitement < 75f -> 1000L
            else -> 700L
        }
    }

    fun maxSimultaneous(act: Int): Int {
        return when (act) {
            1 -> 3
            2 -> 5
            3 -> 7
            else -> 3
        }
    }

    fun spawnObject(boardWidth: Float, act: Int): FallingObject {
        val type = pickObjectType(act)
        val radius = objectRadius(type)
        val minX = radius + 40f
        val maxX = boardWidth - radius - 40f
        val x = if (maxX > minX) {
            Random.nextFloat() * (maxX - minX) + minX
        } else {
            minX
        }
        return spawnAt(type, x, act)
    }
}
