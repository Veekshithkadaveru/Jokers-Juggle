package app.krafted.jokersjuggle.game

import kotlin.random.Random

enum class ChaosEvent { WIND, SPEED_CHANGE, JOKER_THROW, BLACKOUT, MIRROR }

class ChaosEventEngine {
    var windForce: Float = 0f
    var speedMultiplier: Float = 1f
    var gravityMultiplier: Float = 1f
    var screenAlpha: Float = 1f
    var controlsSwapped: Boolean = false

    private var activeEvent: ChaosEvent? = null
    private var remainingMs: Long = 0L

    private var windTriggered = false
    private var blackoutTriggered = false
    private var mirrorTriggered = false
    private var speedChangeTimerMs = 0L
    private var jokerThrowCooldownMs = 0L

    fun update(deltaMs: Long, elapsedMs: Long, excitement: Float, airborneCount: Int): ChaosEvent? {
        if (activeEvent != null) {
            remainingMs -= deltaMs
            if (remainingMs <= 0L) endActiveEvent()
            return null
        }

        if (airborneCount < 4) return null

        if (jokerThrowCooldownMs > 0L) jokerThrowCooldownMs -= deltaMs

        val elapsedSec = (elapsedMs / 1000).toInt()

        if (elapsedSec >= 30 && !windTriggered) {
            windTriggered = true
            return start(ChaosEvent.WIND)
        }

        if (elapsedSec >= 60 && !blackoutTriggered) {
            blackoutTriggered = true
            return start(ChaosEvent.BLACKOUT)
        }

        if (elapsedSec >= 90 && !mirrorTriggered) {
            mirrorTriggered = true
            return start(ChaosEvent.MIRROR)
        }

        speedChangeTimerMs += deltaMs
        if (speedChangeTimerMs >= 45_000L) {
            speedChangeTimerMs = 0L
            return start(ChaosEvent.SPEED_CHANGE)
        }

        if (excitement >= 100f && jokerThrowCooldownMs <= 0L) {
            jokerThrowCooldownMs = 15_000L
            return start(ChaosEvent.JOKER_THROW)
        }

        return null
    }

    fun reset() {
        windForce = 0f
        speedMultiplier = 1f
        gravityMultiplier = 1f
        screenAlpha = 1f
        controlsSwapped = false
        activeEvent = null
        remainingMs = 0L
        windTriggered = false
        blackoutTriggered = false
        mirrorTriggered = false
        speedChangeTimerMs = 0L
        jokerThrowCooldownMs = 0L
    }

    private fun start(event: ChaosEvent): ChaosEvent {
        activeEvent = event
        when (event) {
            ChaosEvent.WIND -> {
                windForce = if (Random.nextBoolean()) 120f else -120f
                remainingMs = 5000L
            }

            ChaosEvent.SPEED_CHANGE -> {
                gravityMultiplier = 1.5f
                remainingMs = 4000L
            }

            ChaosEvent.BLACKOUT -> {
                screenAlpha = 0.2f
                remainingMs = 2000L
            }

            ChaosEvent.MIRROR -> {
                controlsSwapped = true
                remainingMs = 4000L
            }

            ChaosEvent.JOKER_THROW -> {
                remainingMs = 500L
            }
        }
        return event
    }

    private fun endActiveEvent() {
        when (activeEvent) {
            ChaosEvent.WIND -> windForce = 0f
            ChaosEvent.SPEED_CHANGE -> gravityMultiplier = 1f
            ChaosEvent.BLACKOUT -> screenAlpha = 1f
            ChaosEvent.MIRROR -> controlsSwapped = false
            else -> {}
        }
        activeEvent = null
        remainingMs = 0L
    }
}
