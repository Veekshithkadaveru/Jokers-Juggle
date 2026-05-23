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
        val obj = FallingObject(
            type = ObjectType.ORANGE,
            x = 100f,
            y = 500f,
            radius = 30f
        )
        assertTrue(checkCatch(obj, hand))
    }

    @Test
    fun testCheckCatchTooFarHorizontal() {
        val hand = Hand(isLeft = true).apply {
            x = 100f
            y = 500f
        }
        // dx threshold is 65 + 30 * 0.5 = 80
        val objFarX = FallingObject(
            type = ObjectType.ORANGE,
            x = 181f,
            y = 500f,
            radius = 30f
        )
        assertFalse(checkCatch(objFarX, hand))

        val objCloseX = FallingObject(
            type = ObjectType.ORANGE,
            x = 179f,
            y = 500f,
            radius = 30f
        )
        assertTrue(checkCatch(objCloseX, hand))
    }

    @Test
    fun testCheckCatchTooFarVertical() {
        val hand = Hand(isLeft = true).apply {
            x = 100f
            y = 500f
        }
        // dy threshold is 36 + 30 * 0.5 = 51
        val objFarY = FallingObject(
            type = ObjectType.ORANGE,
            x = 100f,
            y = 552f,
            radius = 30f
        )
        assertFalse(checkCatch(objFarY, hand))

        val objCloseY = FallingObject(
            type = ObjectType.ORANGE,
            x = 100f,
            y = 550f,
            radius = 30f
        )
        assertTrue(checkCatch(objCloseY, hand))
    }

    @Test
    fun testCheckCatchAboveGloveTopLimit() {
        val hand = Hand(isLeft = true).apply {
            x = 100f
            y = 500f
        }
        // glove top is hand.y - 36 = 464
        val objAboveGloveTop = FallingObject(
            type = ObjectType.ORANGE,
            x = 100f,
            y = 463f,
            radius = 30f
        )
        assertFalse(checkCatch(objAboveGloveTop, hand))

        val objBelowGloveTop = FallingObject(
            type = ObjectType.ORANGE,
            x = 100f,
            y = 465f,
            radius = 30f
        )
        assertTrue(checkCatch(objBelowGloveTop, hand))
    }
}
