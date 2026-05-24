package app.krafted.jokersjuggle.game

import kotlin.random.Random

enum class ChaosEvent { WIND, SPEED_RUSH, JOKER_THROW, BLACKOUT, MIRROR }

class ChaosEventEngine {
    var windForce: Float = 0f
    var speedMultiplier: Float = 1f
    var screenAlpha: Float = 1f
    var controlsSwapped: Boolean = false

    private var activeEvent: ChaosEvent? = null
    private var remainingMs: Long = 0L

    private var schedulerMs: Long = SCHEDULER_INTERVAL_MS
    private var jokerCooldownMs: Long = 0L
    private var blackoutUsed: Boolean = false
    private var mirrorUsed: Boolean = false

    fun update(deltaMs: Long, act: Int, excitement: Float): ChaosEvent? {
        if (act != 3) return null

        if (jokerCooldownMs > 0L) jokerCooldownMs -= deltaMs

        // While an event is active only count it down; nothing new can start.
        if (activeEvent != null) {
            remainingMs -= deltaMs
            if (remainingMs <= 0L) endActiveEvent()
            return null
        }

        schedulerMs -= deltaMs

        if (excitement > 75f && jokerCooldownMs <= 0L) {
            return start(ChaosEvent.JOKER_THROW)
        }

        if (schedulerMs <= 0L) {
            schedulerMs += SCHEDULER_INTERVAL_MS
            return start(pickScheduledEvent())
        }

        return null
    }

    fun reset() {
        windForce = 0f
        speedMultiplier = 1f
        screenAlpha = 1f
        controlsSwapped = false
        activeEvent = null
        remainingMs = 0L
        schedulerMs = SCHEDULER_INTERVAL_MS
        jokerCooldownMs = 0L
        blackoutUsed = false
        mirrorUsed = false
    }

    private fun pickScheduledEvent(): ChaosEvent {
        val pool = mutableListOf(
            ChaosEvent.WIND, ChaosEvent.WIND, ChaosEvent.WIND,
            ChaosEvent.SPEED_RUSH
        )
        if (!blackoutUsed) pool.add(ChaosEvent.BLACKOUT)
        if (!mirrorUsed) pool.add(ChaosEvent.MIRROR)
        return pool[Random.nextInt(pool.size)]
    }

    private fun start(event: ChaosEvent): ChaosEvent {
        activeEvent = event
        when (event) {
            ChaosEvent.WIND -> {
                windForce = (if (Random.nextBoolean()) 1f else -1f) * 80f
                remainingMs = 5000L
            }
            ChaosEvent.SPEED_RUSH -> {
                speedMultiplier = 2f
                remainingMs = 3000L
            }
            ChaosEvent.JOKER_THROW -> {
                remainingMs = 800L
                jokerCooldownMs = JOKER_COOLDOWN_MS
            }
            ChaosEvent.BLACKOUT -> {
                screenAlpha = 0.2f
                remainingMs = 2000L
                blackoutUsed = true
            }
            ChaosEvent.MIRROR -> {
                controlsSwapped = true
                remainingMs = 4000L
                mirrorUsed = true
            }
        }
        return event
    }

    private fun endActiveEvent() {
        when (activeEvent) {
            ChaosEvent.WIND -> windForce = 0f
            ChaosEvent.SPEED_RUSH -> speedMultiplier = 1f
            ChaosEvent.BLACKOUT -> screenAlpha = 1f
            ChaosEvent.MIRROR -> controlsSwapped = false
            else -> {}
        }
        activeEvent = null
        remainingMs = 0L
    }

    private companion object {
        const val SCHEDULER_INTERVAL_MS = 15000L
        const val JOKER_COOLDOWN_MS = 6000L
    }
}
