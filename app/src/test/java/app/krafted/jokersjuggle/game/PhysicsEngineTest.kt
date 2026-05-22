package app.krafted.jokersjuggle.game

import org.junit.Assert.assertTrue
import org.junit.Test

class PhysicsEngineTest {

    private val boardWidth = 1000f

    @Test
    fun gravityAcceleratesFall() {
        val obj = FallingObject(type = ObjectType.GRAPES, x = 500f, y = 0f)
        var previous = obj.velocityY
        repeat(5) {
            PhysicsEngine.update(obj, boardWidth)
            assertTrue(obj.velocityY > previous)
            previous = obj.velocityY
        }
    }

    @Test
    fun orangeFallsFasterThanGrapes() {
        val orange = FallingObject(type = ObjectType.ORANGE, x = 500f, y = 0f, radius = 40f)
        val grapes = FallingObject(type = ObjectType.GRAPES, x = 500f, y = 0f, radius = 40f)
        repeat(30) {
            PhysicsEngine.update(orange, boardWidth)
            PhysicsEngine.update(grapes, boardWidth)
        }
        assertTrue(orange.y > grapes.y)
    }

    @Test
    fun grapesXOscillates() {
        val obj = FallingObject(type = ObjectType.GRAPES, x = 500f, y = 0f, velocityY = 0f)
        val xs = ArrayList<Float>()
        repeat(80) {
            obj.velocityY = 0f
            PhysicsEngine.update(obj, boardWidth)
            xs.add(obj.x)
        }
        var sawPositive = false
        var sawNegative = false
        for (i in 1 until xs.size) {
            val delta = xs[i] - xs[i - 1]
            if (delta > 0f) sawPositive = true
            if (delta < 0f) sawNegative = true
        }
        assertTrue(sawPositive && sawNegative)
    }

    @Test
    fun wallClamp() {
        val radius = 40f
        val obj = FallingObject(
            type = ObjectType.JOKER_HAT,
            x = boardWidth - radius - 5f,
            y = 0f,
            velocityX = 200f,
            radius = radius
        )
        repeat(20) {
            PhysicsEngine.update(obj, boardWidth)
            assertTrue(obj.x <= boardWidth - obj.radius)
            assertTrue(obj.x >= obj.radius)
        }
    }
}
