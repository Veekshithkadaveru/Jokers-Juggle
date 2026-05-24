package app.krafted.jokersjuggle.game

import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import app.krafted.jokersjuggle.game.CatchDetector.checkCatch

class CatchDetectorTest {

    @Test
    fun testCheckCatchSuccessful() {
        val hand = Hand(isLeft = true).apply {
            x = 100f
            y = 500f
        }
        val obj = JuggleObject(
            id = 1,
            type = ObjectType.ORANGE,
            x = 100f,
            y = 500f,
            velocityX = 0f,
            velocityY = 10f
        )
        assertTrue(checkCatch(obj, hand))
    }

    @Test
    fun testCheckCatchTooFarHorizontal() {
        val hand = Hand(isLeft = true).apply {
            x = 100f
            y = 500f
        }
        val objFarX = JuggleObject(
            id = 1,
            type = ObjectType.ORANGE,
            x = 194f,
            y = 500f,
            velocityX = 0f,
            velocityY = 10f
        )
        assertFalse(checkCatch(objFarX, hand))

        val objCloseX = JuggleObject(
            id = 1,
            type = ObjectType.ORANGE,
            x = 192f,
            y = 500f,
            velocityX = 0f,
            velocityY = 10f
        )
        assertTrue(checkCatch(objCloseX, hand))
    }

    @Test
    fun testCheckCatchTooFarVertical() {
        val hand = Hand(isLeft = true).apply {
            x = 100f
            y = 500f
        }
        val objFarY = JuggleObject(
            id = 1,
            type = ObjectType.ORANGE,
            x = 100f,
            y = 554f,
            velocityX = 0f,
            velocityY = 10f
        )
        assertFalse(checkCatch(objFarY, hand))

        val objCloseY = JuggleObject(
            id = 1,
            type = ObjectType.ORANGE,
            x = 100f,
            y = 552f,
            velocityX = 0f,
            velocityY = 10f
        )
        assertTrue(checkCatch(objCloseY, hand))
    }

    @Test
    fun testCheckCatchMovingUpward() {
        val hand = Hand(isLeft = true).apply {
            x = 100f
            y = 500f
        }
        val objUpward = JuggleObject(
            id = 1,
            type = ObjectType.ORANGE,
            x = 100f,
            y = 500f,
            velocityX = 0f,
            velocityY = -10f
        )
        assertFalse(checkCatch(objUpward, hand))
    }
}
