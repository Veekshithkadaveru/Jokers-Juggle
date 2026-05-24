package app.krafted.jokersjuggle.game

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class JuggleSpawnerTest {

    @Test
    fun testSpawnWeights() {
        val spawner = JuggleSpawner(800f, 1000f)
        val weightsUnder4 = spawner.spawnWeights(3)
        assertEquals(0, weightsUnder4.getOrDefault(ObjectType.GOLD_X, 0))

        val weightsOver4 = spawner.spawnWeights(4)
        assertEquals(7, weightsOver4.getOrDefault(ObjectType.GOLD_X, 0))
    }

    @Test
    fun testLaunchObject() {
        val spawner = JuggleSpawner(800f, 1000f)
        val obj1 = spawner.launchObject(ObjectType.GRAPES)
        assertEquals(1, obj1.id)
        assertEquals(ObjectType.GRAPES, obj1.type)
        assertTrue(obj1.x in 160f..640f)
        assertEquals(850f, obj1.y)

        val obj2 = spawner.launchObject(ObjectType.CHERRIES)
        assertEquals(2, obj2.id)
    }

    @Test
    fun testTick() {
        val spawner = JuggleSpawner(800f, 1000f)
        // Before 15 seconds, no spawn
        assertNull(spawner.tick(5000L, 2))

        // At or after 15 seconds, a spawn occurs
        val spawned = spawner.tick(15000L, 2)
        assertNotNull(spawned)

        // If at max capacity (7), no spawn occurs even if time elapsed
        assertNull(spawner.tick(30000L, 7))
    }

    @Test
    fun testReset() {
        val spawner = JuggleSpawner(800f, 1000f)
        assertNotNull(spawner.tick(15000L, 2))
        // After tick, next spawn is at 30s. So 20s should return null
        assertNull(spawner.tick(20000L, 2))

        spawner.reset()
        // After reset, 15s should trigger a spawn again
        assertNotNull(spawner.tick(15000L, 2))
    }
}
