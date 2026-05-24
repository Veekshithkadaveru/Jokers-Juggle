package app.krafted.jokersjuggle.game

import org.junit.Assert.assertTrue
import org.junit.Test

class PhysicsEngineTest {

    private val boardWidth = 1000f

    @Test
    fun gravityAcceleratesFall() {
        val obj = JuggleObject(
            id = 1,
            type = ObjectType.GRAPES,
            x = 500f,
            y = 0f,
            velocityX = 0f,
            velocityY = 0f
        )
        var previous = obj.velocityY
        repeat(5) {
            PhysicsEngine.update(obj, boardWidth, 0.1f)
            assertTrue(obj.velocityY > previous)
            previous = obj.velocityY
        }
    }

    @Test
    fun orangeFallsFasterThanGrapes() {
        val orange = JuggleObject(
            id = 1,
            type = ObjectType.ORANGE,
            x = 500f,
            y = 0f,
            velocityX = 0f,
            velocityY = 0f
        )
        val grapes = JuggleObject(
            id = 2,
            type = ObjectType.GRAPES,
            x = 500f,
            y = 0f,
            velocityX = 0f,
            velocityY = 0f
        )
        repeat(30) {
            PhysicsEngine.update(orange, boardWidth, 0.1f)
            PhysicsEngine.update(grapes, boardWidth, 0.1f)
        }
        assertTrue(orange.y > grapes.y)
    }

    @Test
    fun grapesXOscillates() {
        val obj = JuggleObject(
            id = 1,
            type = ObjectType.GRAPES,
            x = 500f,
            y = 0f,
            velocityX = 0f,
            velocityY = 0f
        )
        val xs = ArrayList<Float>()
        repeat(80) {
            obj.velocityY = 0f
            PhysicsEngine.update(obj, boardWidth, 0.1f)
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
        val obj = JuggleObject(
            id = 1,
            type = ObjectType.JOKER_HAT,
            x = boardWidth - 45f,
            y = 0f,
            velocityX = 200f,
            velocityY = 0f
        )
        repeat(20) {
            PhysicsEngine.update(obj, boardWidth, 0.1f)
            assertTrue(obj.x <= boardWidth - obj.radius)
            assertTrue(obj.x >= obj.radius)
        }
    }
}
