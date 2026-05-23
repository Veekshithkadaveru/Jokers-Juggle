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

private const val BACKDROP_COLOR = 0xFF0A0408.toInt()
private const val GLOVE_BURGUNDY = 0xFF7A1521.toInt()
private const val GLOVE_GOLD = 0xFFE8B84C.toInt()
private const val GLOVE_HALF_WIDTH = 65f
private const val GLOVE_HALF_HEIGHT = 36f
private const val GLOVE_CORNER = 24f
private const val GLOVE_STROKE = 6f
private const val THUMB_RADIUS = 18f
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

    var act: Int = 1
    private var timeSinceLastSpawnMs: Long = 0L

    var score: Int = 0
    var lives: Int = 3
    var timeLeftMs: Long = 60000L
    val comboTracker = ComboTracker()
    val audienceExcitement = AudienceExcitement()
    var goldMultiplierTimeLeftMs: Long = 0L
    var flashColor: Int? = null
    var flashTimeLeftMs: Long = 0L
    val effects = CopyOnWriteArrayList<GameEffect>()

    var onActComplete: ((completedAct: Int, score: Int) -> Unit)? = null
    var onGameOver: (() -> Unit)? = null

    private val leftHand = Hand(true)
    private val rightHand = Hand(false)
    private val objects = CopyOnWriteArrayList<FallingObject>()

    private var boardWidth = 0f
    private var boardHeight = 0f
    private var gameThread: GameThread? = null

    private var backgrounds: Array<Bitmap>? = null
    private var symbols: Array<Bitmap>? = null

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
        strokeWidth = GLOVE_STROKE
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
    }

    fun addObject(obj: FallingObject) {
        objects.add(obj)
    }

    val excitement: Float
        get() = audienceExcitement.value

    override fun surfaceCreated(holder: SurfaceHolder) {
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

    fun update(deltaSeconds: Float) {
        val deltaMs = (deltaSeconds * 1000f).toLong()
        timeLeftMs = (timeLeftMs - deltaMs).coerceAtLeast(0L)
        if (timeLeftMs <= 0L) {
            endGame(isGameOver = false)
            return
        }

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

        timeSinceLastSpawnMs += deltaMs
        if (timeSinceLastSpawnMs >= JuggleSpawner.getSpawnInterval(excitement) &&
            objects.size < JuggleSpawner.maxSimultaneous(act) &&
            boardWidth > 0f
        ) {
            objects.add(JuggleSpawner.spawnObject(boardWidth, act))
            timeSinceLastSpawnMs = 0L
        }

        leftHand.update(deltaSeconds)
        rightHand.update(deltaSeconds)
        for (obj in objects) {
            PhysicsEngine.update(obj, boardWidth)
        }

        val floorLimit = boardHeight * 0.88f
        for (obj in objects) {
            val leftCaught = CatchDetector.checkCatch(obj, leftHand)
            val rightCaught = CatchDetector.checkCatch(obj, rightHand)
            if (leftCaught || rightCaught) {
                val hand = if (leftCaught) leftHand else rightHand
                handleCatch(obj, hand)
                objects.remove(obj)
            } else if (obj.y > floorLimit) {
                handleDrop(obj)
                objects.remove(obj)
            }
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
    }

    private fun handleCatch(obj: FallingObject, hand: Hand) {
        if (obj.type == ObjectType.GOLD_X) {
            lives--
            comboTracker.resetStreak()
            flashColor = 0x80C91A1A.toInt()
            flashTimeLeftMs = 300L
            effects.add(GameEffect(EffectType.CRACK, obj.x, obj.y, 600L, 600L))
            if (lives <= 0) {
                endGame(isGameOver = true)
            }
            return
        }

        comboTracker.incrementStreak()
        audienceExcitement.onCatch()

        val activeMultiplier = if (goldMultiplierTimeLeftMs > 0L) 2 else 1
        val comboMultiplier = comboTracker.getMultiplier()
        val points = obj.type.points * comboMultiplier * act * activeMultiplier
        score += points

        val textColor = if (comboMultiplier >= 3) GLOVE_GOLD else 0xFFF4E9D8.toInt()
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
            comboMultiplier >= 5 -> {
                effects.add(GameEffect(EffectType.GOLD_BURST, obj.x, obj.y, 600L, 600L))
            }

            comboMultiplier >= 3 -> {
                effects.add(GameEffect(EffectType.ORANGE_RING, obj.x, obj.y, 500L, 500L))
            }

            comboMultiplier >= 2 -> {
                effects.add(GameEffect(EffectType.YELLOW_RING, obj.x, obj.y, 400L, 400L))
            }
        }

        if (obj.type == ObjectType.JOKER_HAT) {
            val allowedTypes = listOf(
                ObjectType.GRAPES,
                ObjectType.CHERRIES,
                ObjectType.ORANGE,
                ObjectType.GOLD_STAR,
                ObjectType.LUCKY_7
            )
            val newType = allowedTypes.random()
            val newObj = FallingObject(
                type = newType,
                x = hand.x,
                y = hand.y - 50f,
                velocityX = (kotlin.random.Random.nextFloat() - 0.5f) * 4f,
                velocityY = -baseFallSpeed(newType, act) * 1.2f,
                isActive = true
            )
            addObject(newObj)
        } else if (obj.type == ObjectType.GOLD_STAR) {
            goldMultiplierTimeLeftMs = 5000L
        } else if (obj.type == ObjectType.LUCKY_7) {
            score += 100 * act
            flashColor = 0x80FFD860.toInt()
            flashTimeLeftMs = 300L
            effects.add(GameEffect(EffectType.CONFETTI, obj.x, obj.y, 1200L, 1200L))
        }
    }

    private fun handleDrop(obj: FallingObject) {
        if (obj.type == ObjectType.GOLD_X) {
            lives--
            flashColor = 0x80C91A1A.toInt()
            flashTimeLeftMs = 300L
            effects.add(GameEffect(EffectType.CRACK, obj.x, obj.y, 700L, 700L))
            if (lives <= 0) {
                endGame(isGameOver = true)
            }
            return
        }

        lives--
        comboTracker.resetStreak()
        audienceExcitement.onDrop()
        effects.add(GameEffect(EffectType.DROP, obj.x, obj.y, 500L, 500L))

        if (lives <= 0) {
            endGame(isGameOver = true)
        }
    }

    private fun endGame(isGameOver: Boolean) {
        gameThread?.running = false
        if (isGameOver) {
            post { onGameOver?.invoke() }
        } else {
            post { onActComplete?.invoke(act, score) }
        }
    }

    private fun backgroundIndex(): Int = when {
        excitement < 25f -> 0
        excitement < 50f -> 1
        excitement < 75f -> 2
        else -> 3
    }

    fun render(canvas: Canvas) {
        canvas.drawColor(BACKDROP_COLOR)
        renderBackground(canvas)
        renderObjects(canvas)
        renderHand(canvas, leftHand)
        renderHand(canvas, rightHand)
        renderEffects(canvas)

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
            destRect.set(
                obj.x - obj.radius,
                obj.y - obj.radius,
                obj.x + obj.radius,
                obj.y + obj.radius
            )
            canvas.drawBitmap(syms[obj.type.ordinal], null, destRect, null)
        }
    }

    private fun renderHand(canvas: Canvas, hand: Hand) {
        canvas.save()
        canvas.rotate((hand.x - hand.prevX) * 0.3f, hand.x, hand.y)
        gloveRect.set(
            hand.x - GLOVE_HALF_WIDTH,
            hand.y - GLOVE_HALF_HEIGHT,
            hand.x + GLOVE_HALF_WIDTH,
            hand.y + GLOVE_HALF_HEIGHT
        )
        if (goldMultiplierTimeLeftMs > 0L) {
            val pulse =
                (kotlin.math.sin(System.currentTimeMillis() / 120.0).toFloat() * 0.5f + 0.5f)
            val alphaGlow = (100 + pulse * 120).toInt().coerceIn(0, 255)
            val glowPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
                style = Paint.Style.STROKE
                color = GLOVE_GOLD
                strokeWidth = GLOVE_STROKE * 2f
                alpha = alphaGlow
            }
            val glowOffset = 10f
            val glowRect = RectF(
                gloveRect.left - glowOffset,
                gloveRect.top - glowOffset,
                gloveRect.right + glowOffset,
                gloveRect.bottom + glowOffset
            )
            canvas.drawRoundRect(
                glowRect,
                GLOVE_CORNER + glowOffset,
                GLOVE_CORNER + glowOffset,
                glowPaint
            )
        }
        canvas.drawRoundRect(gloveRect, GLOVE_CORNER, GLOVE_CORNER, gloveFillPaint)
        canvas.drawRoundRect(gloveRect, GLOVE_CORNER, GLOVE_CORNER, gloveStrokePaint)
        val thumbX = if (hand.isLeft) hand.x + GLOVE_HALF_WIDTH else hand.x - GLOVE_HALF_WIDTH
        val thumbY = hand.y - GLOVE_HALF_HEIGHT
        canvas.drawCircle(thumbX, thumbY, THUMB_RADIUS, gloveFillPaint)
        canvas.drawCircle(thumbX, thumbY, THUMB_RADIUS, gloveStrokePaint)
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
            if (px < boardWidth / 2f) leftHand.targetX = px else rightHand.targetX = px
        }
        if (event.actionMasked == MotionEvent.ACTION_UP) performClick()
        return true
    }

    override fun performClick(): Boolean {
        return super.performClick()
    }
}
