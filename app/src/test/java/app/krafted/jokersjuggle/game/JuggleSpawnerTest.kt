package app.krafted.jokersjuggle.game

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class JuggleSpawnerTest {

    @Test
    fun testGetSpawnInterval() {
        assertEquals(1800L, JuggleSpawner.getSpawnInterval(0f))
        assertEquals(1800L, JuggleSpawner.getSpawnInterval(24.9f))
        assertEquals(1400L, JuggleSpawner.getSpawnInterval(25f))
        assertEquals(1400L, JuggleSpawner.getSpawnInterval(49.9f))
        assertEquals(1000L, JuggleSpawner.getSpawnInterval(50f))
        assertEquals(1000L, JuggleSpawner.getSpawnInterval(74.9f))
        assertEquals(700L, JuggleSpawner.getSpawnInterval(75f))
        assertEquals(700L, JuggleSpawner.getSpawnInterval(100f))
    }

    @Test
    fun testMaxSimultaneous() {
        assertEquals(3, JuggleSpawner.maxSimultaneous(1))
        assertEquals(5, JuggleSpawner.maxSimultaneous(2))
        assertEquals(7, JuggleSpawner.maxSimultaneous(3))
        assertEquals(3, JuggleSpawner.maxSimultaneous(0))
        assertEquals(3, JuggleSpawner.maxSimultaneous(-1))
        assertEquals(3, JuggleSpawner.maxSimultaneous(4))
    }

    @Test
    fun testSpawnObjectBounds() {
        val boardWidth = 800f
        repeat(1000) {
            val obj = JuggleSpawner.spawnObject(boardWidth, 1)
            val radius = objectRadius(obj.type)
            val minX = radius + 40f
            val maxX = boardWidth - radius - 40f
            assertTrue("X (${obj.x}) should be >= minX ($minX)", obj.x >= minX)
            assertTrue("X (${obj.x}) should be <= maxX ($maxX)", obj.x <= maxX)
            assertEquals(-50f, obj.y)
        }
    }

    @Test
    fun testPickObjectTypeWeightsAct1() {
        val counts = mutableMapOf<ObjectType, Int>()
        repeat(100000) {
            val type = JuggleSpawner.pickObjectType(1)
            counts[type] = counts.getOrDefault(type, 0) + 1
        }

        // Expected weights:
        // GRAPES: 30%, CHERRIES: 25%, ORANGE: 20%, JOKER_HAT: 10%, GOLD_STAR: 10%, LUCKY_7: 5%, GOLD_X: 0%
        val grapesRatio = counts.getOrDefault(ObjectType.GRAPES, 0) / 100000f
        val cherriesRatio = counts.getOrDefault(ObjectType.CHERRIES, 0) / 100000f
        val orangeRatio = counts.getOrDefault(ObjectType.ORANGE, 0) / 100000f
        val jokerHatRatio = counts.getOrDefault(ObjectType.JOKER_HAT, 0) / 100000f
        val goldStarRatio = counts.getOrDefault(ObjectType.GOLD_STAR, 0) / 100000f
        val lucky7Ratio = counts.getOrDefault(ObjectType.LUCKY_7, 0) / 100000f
        val goldXCount = counts.getOrDefault(ObjectType.GOLD_X, 0)

        assertTrue(grapesRatio in 0.28f..0.32f)
        assertTrue(cherriesRatio in 0.23f..0.27f)
        assertTrue(orangeRatio in 0.18f..0.22f)
        assertTrue(jokerHatRatio in 0.08f..0.12f)
        assertTrue(goldStarRatio in 0.08f..0.12f)
        assertTrue(lucky7Ratio in 0.03f..0.07f)
        assertEquals(0, goldXCount)
    }

    @Test
    fun testPickObjectTypeWeightsAct2() {
        val counts = mutableMapOf<ObjectType, Int>()
        repeat(100000) {
            val type = JuggleSpawner.pickObjectType(2)
            counts[type] = counts.getOrDefault(type, 0) + 1
        }

        // Expected weights:
        // GRAPES: 22%, CHERRIES: 18%, ORANGE: 18%, JOKER_HAT: 12%, GOLD_STAR: 10%, LUCKY_7: 8%, GOLD_X: 12%
        val grapesRatio = counts.getOrDefault(ObjectType.GRAPES, 0) / 100000f
        val cherriesRatio = counts.getOrDefault(ObjectType.CHERRIES, 0) / 100000f
        val orangeRatio = counts.getOrDefault(ObjectType.ORANGE, 0) / 100000f
        val jokerHatRatio = counts.getOrDefault(ObjectType.JOKER_HAT, 0) / 100000f
        val goldStarRatio = counts.getOrDefault(ObjectType.GOLD_STAR, 0) / 100000f
        val lucky7Ratio = counts.getOrDefault(ObjectType.LUCKY_7, 0) / 100000f
        val goldXRatio = counts.getOrDefault(ObjectType.GOLD_X, 0) / 100000f

        assertTrue(grapesRatio in 0.20f..0.24f)
        assertTrue(cherriesRatio in 0.16f..0.20f)
        assertTrue(orangeRatio in 0.16f..0.20f)
        assertTrue(jokerHatRatio in 0.10f..0.14f)
        assertTrue(goldStarRatio in 0.08f..0.12f)
        assertTrue(lucky7Ratio in 0.06f..0.10f)
        assertTrue(goldXRatio in 0.10f..0.14f)
    }

    @Test
    fun testPickObjectTypeWeightsAct3() {
        val counts = mutableMapOf<ObjectType, Int>()
        repeat(100000) {
            val type = JuggleSpawner.pickObjectType(3)
            counts[type] = counts.getOrDefault(type, 0) + 1
        }

        // Expected weights:
        // GRAPES: 18%, CHERRIES: 15%, ORANGE: 15%, JOKER_HAT: 12%, GOLD_STAR: 8%, LUCKY_7: 7%, GOLD_X: 25%
        val grapesRatio = counts.getOrDefault(ObjectType.GRAPES, 0) / 100000f
        val cherriesRatio = counts.getOrDefault(ObjectType.CHERRIES, 0) / 100000f
        val orangeRatio = counts.getOrDefault(ObjectType.ORANGE, 0) / 100000f
        val jokerHatRatio = counts.getOrDefault(ObjectType.JOKER_HAT, 0) / 100000f
        val goldStarRatio = counts.getOrDefault(ObjectType.GOLD_STAR, 0) / 100000f
        val lucky7Ratio = counts.getOrDefault(ObjectType.LUCKY_7, 0) / 100000f
        val goldXRatio = counts.getOrDefault(ObjectType.GOLD_X, 0) / 100000f

        assertTrue(grapesRatio in 0.16f..0.20f)
        assertTrue(cherriesRatio in 0.13f..0.17f)
        assertTrue(orangeRatio in 0.13f..0.17f)
        assertTrue(jokerHatRatio in 0.10f..0.14f)
        assertTrue(goldStarRatio in 0.06f..0.10f)
        assertTrue(lucky7Ratio in 0.05f..0.09f)
        assertTrue(goldXRatio in 0.23f..0.27f)
    }
}
