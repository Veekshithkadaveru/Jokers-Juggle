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

class JuggleGameView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : SurfaceView(context, attrs), SurfaceHolder.Callback {

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

    var excitement: Float = 0f

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
        leftHand.update(deltaSeconds)
        rightHand.update(deltaSeconds)
        for (obj in objects) {
            PhysicsEngine.update(obj, boardWidth)
        }
        objects.removeAll { it.y > boardHeight + 100f }

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
        canvas.drawRoundRect(gloveRect, GLOVE_CORNER, GLOVE_CORNER, gloveFillPaint)
        canvas.drawRoundRect(gloveRect, GLOVE_CORNER, GLOVE_CORNER, gloveStrokePaint)
        val thumbX = if (hand.isLeft) hand.x + GLOVE_HALF_WIDTH else hand.x - GLOVE_HALF_WIDTH
        val thumbY = hand.y - GLOVE_HALF_HEIGHT
        canvas.drawCircle(thumbX, thumbY, THUMB_RADIUS, gloveFillPaint)
        canvas.drawCircle(thumbX, thumbY, THUMB_RADIUS, gloveStrokePaint)
        canvas.restore()
        hand.prevX = hand.x
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
