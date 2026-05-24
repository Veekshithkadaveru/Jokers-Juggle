package app.krafted.jokersjuggle.game

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.SurfaceHolder
import android.view.SurfaceView
import app.krafted.jokersjuggle.R
import java.util.concurrent.CopyOnWriteArrayList
import kotlin.math.ceil

private const val BACKDROP_COLOR = 0xFF0A0408.toInt()
private const val GLOVE_BURGUNDY = 0xFF7A1521.toInt()
private const val GLOVE_GOLD = 0xFFE8B84C.toInt()
private const val CROSSFADE_DURATION = 0.6f

enum class EffectType { SCORE, CRACK, DROP, YELLOW_RING, ORANGE_RING, GOLD_BURST, CONFETTI }

data class GameEffect(
    val type: EffectType,
    val x: Float,
    val y: Float,
    val durationMs: Long,
    var timeLeftMs: Long,
    val valueText: String = "",
    val color: Int = 0
)

class JuggleGameView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : SurfaceView(context, attrs), SurfaceHolder.Callback {

    private var elapsedMs: Long = 0L
    private var scoreTickTimerMs = 0L
    private var streak5ObjTimerMs = 0L

    var score: Int = 0
    var lives: Int = 3
    var maxObjectsStreak: Int = 0
    var maxObjectsReached: Int = 2
    
    val audienceExcitement = AudienceExcitement()
    private val chaosEngine = ChaosEventEngine()
    var goldMultiplierTimeLeftMs: Long = 0L
    var flashColor: Int? = null
    var flashTimeLeftMs: Long = 0L
    val effects = CopyOnWriteArrayList<GameEffect>()

    var onGameOver: ((score: Int, elapsedSeconds: Int, maxObjectsReached: Int) -> Unit)? = null
    var onStateSnapshot: ((GameSnapshot) -> Unit)? = null
    var onJokerEvent: ((JokerEvent) -> Unit)? = null

    private var gameStartFired = false

    private val leftHand = Hand(true)
    private val rightHand = Hand(false)
    private val objects = CopyOnWriteArrayList<JuggleObject>()

    private var boardWidth = 0f
    private var boardHeight = 0f
    private var gameThread: GameThread? = null

    private var backgrounds: Array<Bitmap>? = null
    private var symbols: Array<Bitmap>? = null
    private var spawner: JuggleSpawner? = null
    private var gloveBitmap: Bitmap? = null
    private var gloveGripBitmap: Bitmap? = null
    private var gloveThrowBitmap: Bitmap? = null

    private var prevIndex = 0
    private var crossProgress = 0f

    private val destRect = RectF()
    private val crossfadePaint = Paint(Paint.FILTER_BITMAP_FLAG)
    private val gloveFillPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
        color = GLOVE_BURGUNDY
    }
    private val gloveStrokePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.STROKE
        color = GLOVE_GOLD
        strokeWidth = 6f
    }
    private val gloveRect = RectF()

    private val effectTextPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        textAlign = Paint.Align.CENTER
        textSize = 40f
        isFakeBoldText = true
    }
    private val effectRingPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.STROKE
    }
    private val effectFillPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
    }
    private val dimPaint = Paint().apply {
        color = 0xFF000000.toInt()
    }

    init {
        holder.addCallback(this)
        isFocusable = true
        loadBitmaps()
    }

    private fun loadBitmaps() {
        if (backgrounds == null) {
            backgrounds = arrayOf(
                BitmapFactory.decodeResource(resources, R.drawable.jok021_back_1),
                BitmapFactory.decodeResource(resources, R.drawable.jok021_back_2),
                BitmapFactory.decodeResource(resources, R.drawable.jok021_back_3),
                BitmapFactory.decodeResource(resources, R.drawable.jok021_back_4)
            )
        }
        if (symbols == null) {
            symbols = arrayOf(
                BitmapFactory.decodeResource(resources, R.drawable.jok021_sym_1),
                BitmapFactory.decodeResource(resources, R.drawable.jok021_sym_2),
                BitmapFactory.decodeResource(resources, R.drawable.jok021_sym_3),
                BitmapFactory.decodeResource(resources, R.drawable.jok021_sym_4),
                BitmapFactory.decodeResource(resources, R.drawable.jok021_sym_5),
                BitmapFactory.decodeResource(resources, R.drawable.jok021_sym_6),
                BitmapFactory.decodeResource(resources, R.drawable.jok021_sym_7)
            )
        }
        if (gloveBitmap == null) {
            gloveBitmap = BitmapFactory.decodeResource(resources, R.drawable.jok021_glove)
        }
        if (gloveGripBitmap == null) {
            gloveGripBitmap = BitmapFactory.decodeResource(resources, R.drawable.jok021_glove_grip)
        }
        if (gloveThrowBitmap == null) {
            gloveThrowBitmap = BitmapFactory.decodeResource(resources, R.drawable.jok021_glove_throw)
        }
    }

    val excitement: Float
        get() = audienceExcitement.value

    override fun surfaceCreated(holder: SurfaceHolder) {
        chaosEngine.reset()
        gameThread = GameThread(holder, this).also {
            it.running = true
            it.start()
        }
    }

    override fun surfaceChanged(holder: SurfaceHolder, format: Int, width: Int, height: Int) {
        boardWidth = width.toFloat()
        boardHeight = height.toFloat()
        
        leftHand.y = boardHeight * 0.88f
        rightHand.y = boardHeight * 0.88f
        leftHand.x = boardWidth * 0.25f
        leftHand.targetX = boardWidth * 0.25f
        leftHand.prevX = boardWidth * 0.25f
        rightHand.x = boardWidth * 0.75f
        rightHand.targetX = boardWidth * 0.75f
        rightHand.prevX = boardWidth * 0.75f

        if (spawner == null) {
            spawner = JuggleSpawner(boardWidth, boardHeight)
            resetGame()
        }
    }

    override fun surfaceDestroyed(holder: SurfaceHolder) {
        gameThread?.running = false
        var retry = true
        while (retry) {
            try {
                gameThread?.join()
                retry = false
            } catch (e: InterruptedException) {
            }
        }
    }

    fun resetGame() {
        score = 0
        lives = 3
        elapsedMs = 0L
        scoreTickTimerMs = 0L
        streak5ObjTimerMs = 0L
        maxObjectsStreak = 0
        maxObjectsReached = 2
        
        leftHand.reset()
        rightHand.reset()
        
        spawner?.reset()
        objects.clear()
        audienceExcitement.reset()
        effects.clear()
        
        spawner?.let { sp ->
            objects.add(sp.launchObject(ObjectType.GRAPES))
            objects.add(sp.launchObject(ObjectType.CHERRIES))
        }

        gameStartFired = false
    }

    fun update(deltaSeconds: Float) {
        if (!gameStartFired) {
            gameStartFired = true
            onJokerEvent?.invoke(JokerEvent.GAME_START)
        }
        val deltaMs = (deltaSeconds * 1000f).toLong()
        elapsedMs += deltaMs

        if (goldMultiplierTimeLeftMs > 0L) {
            goldMultiplierTimeLeftMs = (goldMultiplierTimeLeftMs - deltaMs).coerceAtLeast(0L)
        }
        if (flashTimeLeftMs > 0L) {
            flashTimeLeftMs = (flashTimeLeftMs - deltaMs).coerceAtLeast(0L)
            if (flashTimeLeftMs <= 0L) {
                flashColor = null
            }
        }

        for (eff in effects) {
            eff.timeLeftMs = (eff.timeLeftMs - deltaMs).coerceAtLeast(0L)
        }
        effects.removeIf { it.timeLeftMs <= 0L }

        val chaosStarted = chaosEngine.update(deltaMs, elapsedMs, excitement, objects.size)
        if (chaosStarted != null) {
            val event = when (chaosStarted) {
                ChaosEvent.WIND -> JokerEvent.CHAOS_WIND
                ChaosEvent.SPEED_CHANGE -> JokerEvent.CHAOS_RUSH
                ChaosEvent.JOKER_THROW -> JokerEvent.CHAOS_RUSH
                ChaosEvent.BLACKOUT -> JokerEvent.CHAOS_DARK
                ChaosEvent.MIRROR -> JokerEvent.CHAOS_MIRROR
            }
            onJokerEvent?.invoke(event)
            if (chaosStarted == ChaosEvent.JOKER_THROW && boardWidth > 0f) {
                spawner?.let { sp ->
                    if (objects.size < 7) {
                        objects.add(sp.launchObject(sp.spawnWeights(objects.size).keys.random()))
                    }
                    if (objects.size < 7) {
                        objects.add(sp.launchObject(sp.spawnWeights(objects.size).keys.random()))
                    }
                    triggerObjectAddedEvent(objects.size)
                }
            }
        }

        spawner?.let { sp ->
            val spawnedType = sp.tick(elapsedMs, objects.size)
            if (spawnedType != null) {
                objects.add(sp.launchObject(spawnedType))
                triggerObjectAddedEvent(objects.size)
            }
        }

        audienceExcitement.update(objects.size, dropped = false, deltaSeconds)

        leftHand.update(deltaSeconds)
        rightHand.update(deltaSeconds)

        if (leftHand.justReleased) {
            val caughtObj = objects.find { it.caughtByHand == leftHand }
            if (caughtObj != null) {
                handleThrow(caughtObj, leftHand)
            }
        }
        if (rightHand.justReleased) {
            val caughtObj = objects.find { it.caughtByHand == rightHand }
            if (caughtObj != null) {
                handleThrow(caughtObj, rightHand)
            }
        }
        
        for (obj in objects) {
            val hand = obj.caughtByHand
            if (hand == null) {
                PhysicsEngine.update(obj, boardWidth, deltaSeconds, chaosEngine.gravityMultiplier)
                obj.x += chaosEngine.windForce * deltaSeconds
            } else {
                obj.x = hand.x
                obj.y = hand.y + hand.offsetY - 20f
                obj.velocityX = 0f
                obj.velocityY = 0f
            }
        }

        for (obj in objects) {
            if (obj.caughtByHand == null) {
                val leftCaught = if (leftHand.state == HandState.IDLE) CatchDetector.checkCatch(obj, leftHand) else false
                val rightCaught = if (rightHand.state == HandState.IDLE) CatchDetector.checkCatch(obj, rightHand) else false
                if (leftCaught || rightCaught) {
                    val hand = if (leftCaught) leftHand else rightHand
                    handleCatch(obj, hand)
                } else if (obj.y > boardHeight + obj.radius) {
                    handleDrop(obj)
                }
            }
        }

        if (objects.size > maxObjectsReached) {
            maxObjectsReached = objects.size
        }

        scoreTickTimerMs += deltaMs
        if (scoreTickTimerMs >= 1000L) {
            scoreTickTimerMs -= 1000L
            onScoreTick()
        }

        if (objects.size >= 5) {
            streak5ObjTimerMs += deltaMs
            if (streak5ObjTimerMs >= 30_000L) {
                streak5ObjTimerMs = -99999999L
                onJokerEvent?.invoke(JokerEvent.STREAK_5_OBJ_30S)
            }
        } else {
            streak5ObjTimerMs = 0L
        }

        val target = backgroundIndex()
        if (target != prevIndex) {
            crossProgress += deltaSeconds / CROSSFADE_DURATION
            if (crossProgress >= 1f) {
                crossProgress = 1f
                prevIndex = target
                crossProgress = 0f
            }
        }

        val nextSpawnTarget = ((elapsedMs / 15000L) + 1) * 15000L
        val countdownSeconds = ceil((nextSpawnTarget - elapsedMs) / 1000.0).toInt().coerceIn(1, 15)

        onStateSnapshot?.invoke(
            GameSnapshot(
                score = score,
                lives = lives,
                airborneCount = objects.size,
                multiplier = getMultiplier(objects.size),
                elapsedSeconds = (elapsedMs / 1000).toInt(),
                nextObjectCountdown = if (objects.size >= 7) 0 else countdownSeconds,
                excitement = audienceExcitement.value,
                isMultiplierActive = goldMultiplierTimeLeftMs > 0L,
                multiplierSecondsLeft = ceil(goldMultiplierTimeLeftMs / 1000.0).toInt(),
                screenAlpha = chaosEngine.screenAlpha,
                controlsSwapped = chaosEngine.controlsSwapped,
                isGameOver = false,
                bestScore = 0,
                maxObjectsReached = maxObjectsReached
            )
        )
    }

    private fun onScoreTick() {
        val basePoints = 10
        val multiplier = getMultiplier(objects.size)
        score += basePoints * multiplier

        if (objects.size == 7) {
            maxObjectsStreak++
            if (maxObjectsStreak % 5 == 0) {
                score += 100
                onJokerEvent?.invoke(JokerEvent.OBJECT_ADDED_7)
            }
        } else {
            maxObjectsStreak = 0
        }
    }

    fun getMultiplier(airborneCount: Int): Int = when (airborneCount) {
        1 -> 1
        2 -> 1
        3 -> 2
        4 -> 3
        5 -> 4
        6 -> 5
        else -> 7
    }

    private fun handleCatch(obj: JuggleObject, hand: Hand) {
        if (obj.type == ObjectType.GOLD_X) {
            lives--
            hand.triggerCatchAnimation(isBomb = true)
            objects.remove(obj)
            flashColor = 0x80C91A1A.toInt()
            flashTimeLeftMs = 300L
            effects.add(GameEffect(EffectType.CRACK, obj.x, obj.y, 600L, 600L))
            onJokerEvent?.invoke(JokerEvent.GOLD_X_CAUGHT)
            
            if (lives <= 0) {
                endGame()
            } else {
                onJokerEvent?.invoke(JokerEvent.LIFE_LOST)
                if (lives == 1) onJokerEvent?.invoke(JokerEvent.LAST_LIFE)
            }
            return
        }

        obj.caughtByHand = hand
        hand.triggerCatchAnimation(isBomb = false)
    }

    private fun handleThrow(obj: JuggleObject, hand: Hand) {
        val isPerfect = ThrowEngine.calculateThrow(obj, hand, boardWidth)
        val currentMultiplier = getMultiplier(objects.size)
        
        obj.caughtByHand = null

        if (isPerfect) {
            score += 25 * currentMultiplier
            effects.add(GameEffect(EffectType.GOLD_BURST, obj.x, obj.y, 600L, 600L))
        }

        when (obj.type) {
            ObjectType.JOKER_HAT -> {
                score += 30 * currentMultiplier
                effects.add(GameEffect(EffectType.YELLOW_RING, obj.x, obj.y, 400L, 400L))
                
                if (objects.size < 7) {
                    val allowedTypes = listOf(
                        ObjectType.GRAPES,
                        ObjectType.CHERRIES,
                        ObjectType.ORANGE,
                        ObjectType.GOLD_STAR,
                        ObjectType.LUCKY_7
                    )
                    val newType = allowedTypes.random()
                    spawner?.let { sp ->
                        val newObj = sp.launchObject(newType)
                        newObj.x = hand.x
                        newObj.y = hand.y - 50f
                        newObj.velocityY = -1050f
                        objects.add(newObj)
                        triggerObjectAddedEvent(objects.size)
                    }
                }
                onJokerEvent?.invoke(JokerEvent.JOKER_HAT_CAUGHT)
            }
            ObjectType.GOLD_STAR -> {
                score += 50
                goldMultiplierTimeLeftMs = 8000L
                effects.add(GameEffect(EffectType.GOLD_BURST, obj.x, obj.y, 600L, 600L))
                onJokerEvent?.invoke(JokerEvent.COMBO_10)
            }
            ObjectType.LUCKY_7 -> {
                score += 200 * currentMultiplier
                flashColor = 0x80FFD860.toInt()
                flashTimeLeftMs = 300L
                effects.add(GameEffect(EffectType.CONFETTI, obj.x, obj.y, 1200L, 1200L))
                onJokerEvent?.invoke(JokerEvent.LUCKY_7_CAUGHT)
            }
            else -> {
                val basePoints = obj.type.points
                val activeMultiplier = if (goldMultiplierTimeLeftMs > 0L) 2 else 1
                val points = basePoints * currentMultiplier * activeMultiplier
                score += points

                val textColor = if (currentMultiplier >= 3) GLOVE_GOLD else 0xFFF4E9D8.toInt()
                effects.add(
                    GameEffect(
                        EffectType.SCORE,
                        hand.x,
                        hand.y - 40f,
                        700L,
                        700L,
                        "+$points",
                        textColor
                    )
                )

                when {
                    currentMultiplier >= 5 -> effects.add(GameEffect(EffectType.GOLD_BURST, obj.x, obj.y, 600L, 600L))
                    currentMultiplier >= 3 -> effects.add(GameEffect(EffectType.ORANGE_RING, obj.x, obj.y, 500L, 500L))
                    currentMultiplier >= 2 -> effects.add(GameEffect(EffectType.YELLOW_RING, obj.x, obj.y, 400L, 400L))
                }
            }
        }
    }

    private fun handleDrop(obj: JuggleObject) {
        objects.remove(obj)
        val isBomb = obj.type == ObjectType.GOLD_X
        if (isBomb) {
            onJokerEvent?.invoke(JokerEvent.GOLD_X_DROPPED)
            return
        }

        lives--
        audienceExcitement.update(objects.size, dropped = true, 0f)
        effects.add(GameEffect(EffectType.DROP, obj.x, boardHeight * 0.88f, 500L, 500L))
        onJokerEvent?.invoke(JokerEvent.DROP)

        if (lives <= 0 || objects.isEmpty()) {
            endGame()
        } else {
            onJokerEvent?.invoke(JokerEvent.LIFE_LOST)
            if (lives == 1) onJokerEvent?.invoke(JokerEvent.LAST_LIFE)
        }
    }

    private fun triggerObjectAddedEvent(count: Int) {
        when (count) {
            3 -> onJokerEvent?.invoke(JokerEvent.OBJECT_ADDED_3)
            5 -> onJokerEvent?.invoke(JokerEvent.OBJECT_ADDED_5)
            7 -> onJokerEvent?.invoke(JokerEvent.OBJECT_ADDED_7)
        }
    }

    private fun endGame() {
        gameThread?.running = false
        val isLow = maxObjectsReached <= 2
        val gameOverEvent = if (isLow) JokerEvent.GAME_OVER_LOW else JokerEvent.GAME_OVER_HIGH
        onJokerEvent?.invoke(gameOverEvent)
        post {
            onGameOver?.invoke(score, (elapsedMs / 1000).toInt(), maxObjectsReached)
        }
    }

    private fun backgroundIndex(): Int = audienceExcitement.getBackgroundIndex()

    fun render(canvas: Canvas) {
        canvas.drawColor(BACKDROP_COLOR)
        renderBackground(canvas)
        renderObjects(canvas)
        renderHand(canvas, leftHand)
        renderHand(canvas, rightHand)
        renderEffects(canvas)

        if (chaosEngine.screenAlpha < 1f) {
            dimPaint.alpha = ((1f - chaosEngine.screenAlpha) * 255f).toInt()
            canvas.drawRect(0f, 0f, boardWidth, boardHeight, dimPaint)
        }

        val color = flashColor
        if (color != null) {
            canvas.drawColor(color)
        }
    }

    private fun renderBackground(canvas: Canvas) {
        val bgs = backgrounds ?: return
        val target = backgroundIndex()
        destRect.set(0f, 0f, boardWidth, boardHeight)
        if (target == prevIndex) {
            canvas.drawBitmap(bgs[prevIndex], null, destRect, null)
            return
        }
        canvas.drawBitmap(bgs[prevIndex], null, destRect, null)
        crossfadePaint.alpha = (crossProgress * 255).toInt()
        canvas.drawBitmap(bgs[target], null, destRect, crossfadePaint)
    }

    private fun renderObjects(canvas: Canvas) {
        val syms = symbols ?: return
        for (obj in objects) {
            if (obj.caughtByHand != null) continue
            destRect.set(
                obj.x - obj.radius,
                obj.y - obj.radius,
                obj.x + obj.radius,
                obj.y + obj.radius
            )
            canvas.save()
            canvas.rotate(obj.rotationAngle, obj.x, obj.y)
            canvas.drawBitmap(syms[obj.type.ordinal], null, destRect, null)
            canvas.restore()
        }
    }

    private fun renderHand(canvas: Canvas, hand: Hand) {
        val glove = when (hand.state) {
            HandState.CATCH -> gloveGripBitmap
            HandState.THROW -> gloveThrowBitmap
            else -> gloveBitmap
        } ?: return
        canvas.save()

        val tiltDegrees = (hand.velocity * 0.08f).coerceIn(-25f, 25f)
        canvas.rotate(tiltDegrees, hand.x, hand.y)

        if (hand.isLeft) {
            canvas.scale(-1f, 1f, hand.x, hand.y)
        }

        canvas.scale(hand.scaleX, hand.scaleY, hand.x, hand.y + hand.offsetY)

        val w = hand.catchWidth
        val h = hand.catchHeight

        gloveRect.set(
            hand.x - w / 2f,
            hand.y + hand.offsetY - h / 2f,
            hand.x + w / 2f,
            hand.y + hand.offsetY + h / 2f
        )

        val currentMultiplier = getMultiplier(objects.size)
        val hasGlow = currentMultiplier >= 2 || goldMultiplierTimeLeftMs > 0L
        if (hasGlow) {
            val glowColor = when {
                goldMultiplierTimeLeftMs > 0L -> 0xFFFFD860.toInt()
                currentMultiplier >= 5 -> 0xFFFFD860.toInt()
                currentMultiplier >= 3 -> 0xFFFF8030.toInt()
                else -> 0xFFFFD860.toInt()
            }

            val pulse = (kotlin.math.sin(System.currentTimeMillis() / 120.0).toFloat() * 0.2f + 0.8f)
            val alphaGlow = (100 + pulse * 120).toInt().coerceIn(0, 255)
            val glowPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
                style = Paint.Style.STROKE
                color = glowColor
                strokeWidth = 12f
                alpha = alphaGlow
            }
            val glowOffset = 10f
            val glowRect = RectF(
                gloveRect.left - glowOffset,
                gloveRect.top - glowOffset,
                gloveRect.right + glowOffset,
                gloveRect.bottom + glowOffset
            )
            canvas.drawRoundRect(glowRect, 24f + glowOffset, 24f + glowOffset, glowPaint)
        }

        canvas.drawBitmap(glove, null, gloveRect, null)

        val caughtObj = objects.find { it.caughtByHand == hand }
        if (caughtObj != null) {
            val syms = symbols
            if (syms != null) {
                canvas.save()
                if (hand.isLeft) {
                    canvas.scale(-1f, 1f, hand.x, hand.y)
                }
                val r = caughtObj.radius
                destRect.set(
                    hand.x - r,
                    hand.y + hand.offsetY - r - 20f,
                    hand.x + r,
                    hand.y + hand.offsetY + r - 20f
                )
                canvas.drawBitmap(syms[caughtObj.type.ordinal], null, destRect, null)
                canvas.restore()
            }
        }

        canvas.restore()
        hand.prevX = hand.x
    }

    private fun renderEffects(canvas: Canvas) {
        for (eff in effects) {
            val age = (eff.durationMs - eff.timeLeftMs).toFloat() / eff.durationMs.toFloat()
            val alphaVal = ((1f - age) * 255).toInt().coerceIn(0, 255)

            when (eff.type) {
                EffectType.SCORE -> {
                    effectTextPaint.color = eff.color
                    effectTextPaint.alpha = alphaVal
                    val floatOffset = age * 50f
                    canvas.drawText(eff.valueText, eff.x, eff.y - floatOffset, effectTextPaint)
                }

                EffectType.YELLOW_RING -> {
                    effectRingPaint.color = 0xFFFFD860.toInt()
                    effectRingPaint.alpha = alphaVal
                    effectRingPaint.strokeWidth = 6f
                    val radius = 10f + age * 50f
                    canvas.drawCircle(eff.x, eff.y, radius, effectRingPaint)
                }

                EffectType.ORANGE_RING -> {
                    effectRingPaint.color = 0xFFFF8030.toInt()
                    effectRingPaint.alpha = alphaVal
                    effectRingPaint.strokeWidth = 8f
                    val radius = 10f + age * 60f
                    canvas.drawCircle(eff.x, eff.y, radius, effectRingPaint)

                    effectFillPaint.color = 0xFFFF9030.toInt()
                    effectFillPaint.alpha = alphaVal
                    val dist = age * 60f
                    for (k in 0 until 8) {
                        val angle = (k / 8.0f) * 2f * 3.1415927f
                        val dotX = eff.x + kotlin.math.cos(angle) * dist
                        val dotY = eff.y + kotlin.math.sin(angle) * dist
                        canvas.drawCircle(dotX, dotY, 8f, effectFillPaint)
                    }
                }

                EffectType.GOLD_BURST -> {
                    effectRingPaint.color = 0xFFFFD860.toInt()
                    effectRingPaint.alpha = alphaVal
                    effectRingPaint.strokeWidth = 10f
                    val radius = 12f + age * 80f
                    canvas.drawCircle(eff.x, eff.y, radius, effectRingPaint)

                    effectFillPaint.color = 0xFFFFD860.toInt()
                    effectFillPaint.alpha = alphaVal
                    val dist = age * 100f
                    for (k in 0 until 12) {
                        val angle = (k / 12.0f) * 2f * 3.1415927f
                        val dotX = eff.x + kotlin.math.cos(angle) * dist
                        val dotY = eff.y + kotlin.math.sin(angle) * dist
                        canvas.drawCircle(dotX, dotY, 10f, effectFillPaint)
                    }
                }

                EffectType.CRACK -> {
                    effectFillPaint.color = 0xFFC91A1A.toInt()
                    effectFillPaint.alpha = alphaVal
                    val w = 60f
                    val h = 6f
                    val crackRect = RectF(
                        eff.x - w / 2f,
                        eff.y - h / 2f,
                        eff.x + w / 2f,
                        eff.y + h / 2f
                    )
                    canvas.drawRect(crackRect, effectFillPaint)
                }

                EffectType.DROP -> {
                    effectFillPaint.color = 0xFF3A0408.toInt()
                    effectFillPaint.alpha = alphaVal
                    val rx = 30f * (1f + age * 1.5f)
                    val ry = 4f
                    val splashRect = RectF(eff.x - rx, eff.y - ry, eff.x + rx, eff.y + ry)
                    canvas.drawOval(splashRect, effectFillPaint)
                }

                EffectType.CONFETTI -> {
                    val colors = intArrayOf(
                        0xFFFFD860.toInt(),
                        0xFFC91A1A.toInt(),
                        0xFF7A2DB0.toInt(),
                        0xFF3A8A4A.toInt(),
                        0xFFF4E9D8.toInt()
                    )
                    val dist = age * 200f
                    for (k in 0 until 20) {
                        val angle = (k / 20.0f) * 2f * 3.1415927f + age
                        val cx = eff.x + kotlin.math.cos(angle) * dist
                        val cy = eff.y + kotlin.math.sin(angle) * dist
                        effectFillPaint.color = colors[k % colors.size]
                        effectFillPaint.alpha = alphaVal

                        canvas.save()
                        canvas.translate(cx, cy)
                        canvas.rotate(age * 720f)
                        val w = 12f
                        val h = 20f
                        canvas.drawRect(-w / 2f, -h / 2f, w / 2f, h / 2f, effectFillPaint)
                        canvas.restore()
                    }
                }
            }
        }
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        for (i in 0 until event.pointerCount) {
            val px = event.getX(i)
            val leftSide = px < boardWidth / 2f
            val targetLeftHand = if (chaosEngine.controlsSwapped) !leftSide else leftSide
            if (targetLeftHand) {
                leftHand.targetX = px
            } else {
                rightHand.targetX = px
            }
        }
        if (event.actionMasked == MotionEvent.ACTION_UP) performClick()
        return true
    }

    override fun performClick(): Boolean {
        return super.performClick()
    }
}
