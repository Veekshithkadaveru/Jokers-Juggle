package app.krafted.jokersjuggle.game

import android.graphics.Canvas
import android.view.SurfaceHolder

class GameThread(
    private val surfaceHolder: SurfaceHolder,
    private val gameView: JuggleGameView
) : Thread() {

    @Volatile
    var running: Boolean = false

    override fun run() {
        var last = System.nanoTime()
        while (running) {
            val frameStart = System.nanoTime()
            val dt = ((frameStart - last) / 1_000_000_000f).coerceAtMost(0.05f)
            last = frameStart

            var canvas: Canvas? = null
            try {
                canvas = surfaceHolder.lockCanvas()
                if (canvas != null) {
                    synchronized(surfaceHolder) {
                        gameView.update(dt)
                        gameView.render(canvas)
                    }
                }
            } finally {
                if (canvas != null) surfaceHolder.unlockCanvasAndPost(canvas)
            }

            val remaining = TARGET_FRAME_NANOS - (System.nanoTime() - frameStart)
            if (remaining > 0) {
                try {
                    sleep(remaining / 1_000_000L, (remaining % 1_000_000L).toInt())
                } catch (e: InterruptedException) {
                    break
                }
            }
        }
    }

    companion object {
        const val TARGET_FRAME_NANOS: Long = 1_000_000_000L / 60L
    }
}
