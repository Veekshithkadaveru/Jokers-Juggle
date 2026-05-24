package app.krafted.jokersjuggle.ui.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.spring
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import app.krafted.jokersjuggle.R
import app.krafted.jokersjuggle.ui.theme.Burgundy
import app.krafted.jokersjuggle.ui.theme.CreamText
import app.krafted.jokersjuggle.ui.theme.DeepStage
import app.krafted.jokersjuggle.ui.theme.Gold
import app.krafted.jokersjuggle.ui.theme.MarqueeDim
import app.krafted.jokersjuggle.viewmodel.GameUiState
import app.krafted.jokersjuggle.viewmodel.JokerExpression
import kotlinx.coroutines.delay

private enum class EyeShape { DIAMOND, WIDE, NARROW, CLOSED }
private enum class MouthShape { LINE, SMIRK, GRIN, OPEN, FROWN }

private data class FaceSpec(
    val eyeColor: Color,
    val eye: EyeShape,
    val mouth: MouthShape,
    val tilt: Float,
)

private fun faceFor(expression: JokerExpression): FaceSpec = when (expression) {
    JokerExpression.NEUTRAL -> FaceSpec(Gold, EyeShape.DIAMOND, MouthShape.LINE, 0f)
    JokerExpression.AMUSED -> FaceSpec(Color(0xFFFFD860), EyeShape.DIAMOND, MouthShape.SMIRK, -2f)
    JokerExpression.IMPRESSED -> FaceSpec(Color(0xFFFFD860), EyeShape.WIDE, MouthShape.SMIRK, 0f)
    JokerExpression.GLEEFUL -> FaceSpec(Color(0xFFFFEC80), EyeShape.WIDE, MouthShape.GRIN, 2f)
    JokerExpression.SINISTER -> FaceSpec(Color(0xFFFF4A30), EyeShape.NARROW, MouthShape.FROWN, -3f)
    JokerExpression.LAUGHING -> FaceSpec(Color(0xFFFFD860), EyeShape.CLOSED, MouthShape.OPEN, 3f)
    JokerExpression.UNHINGED -> FaceSpec(Color(0xFFFF5A20), EyeShape.WIDE, MouthShape.GRIN, 4f)
    JokerExpression.ECSTATIC -> FaceSpec(Color(0xFFFFF5B0), EyeShape.WIDE, MouthShape.OPEN, 0f)
    JokerExpression.THEATRICAL -> FaceSpec(Gold, EyeShape.DIAMOND, MouthShape.SMIRK, 0f)
    JokerExpression.TRIUMPHANT -> FaceSpec(Color(0xFFFFD860), EyeShape.DIAMOND, MouthShape.SMIRK, -1f)
    JokerExpression.GENUINE -> FaceSpec(Color(0xFFFFF0C0), EyeShape.DIAMOND, MouthShape.SMIRK, 0f)
    JokerExpression.SURPRISED -> FaceSpec(Color(0xFFFFEC80), EyeShape.WIDE, MouthShape.OPEN, 0f)
    JokerExpression.TAUNTING -> FaceSpec(Color(0xFFFF4A30), EyeShape.NARROW, MouthShape.SMIRK, -2f)
    JokerExpression.RESPECTFUL -> FaceSpec(Color(0xFFFFF0C0), EyeShape.DIAMOND, MouthShape.LINE, 0f)
}

@Composable
fun JokerReactionOverlay(state: GameUiState, modifier: Modifier = Modifier) {
    Box(modifier) {
        Portrait(
            state.jokerExpression,
            Modifier
                .align(Alignment.TopEnd)
                .padding(top = 56.dp, end = 8.dp),
        )
        QuoteBar(
            state.jokerQuote,
            Modifier
                .align(Alignment.BottomStart)
                .fillMaxWidth(0.78f)
                .padding(start = 10.dp, bottom = 220.dp),
        )
    }
}

@Composable
private fun Portrait(expression: JokerExpression, modifier: Modifier) {
    val spec = faceFor(expression)
    val pulse = remember { Animatable(1f) }
    LaunchedEffect(expression) {
        pulse.snapTo(1f)
        pulse.animateTo(1.18f, spring())
        pulse.animateTo(1f, spring())
    }
    val hat = ImageBitmap.imageResource(R.drawable.jok021_sym_4)

    Column(
        modifier = modifier.scale(pulse.value),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Box(
            Modifier
                .width(64.dp)
                .height(80.dp)
                .background(DeepStage, RoundedCornerShape(8.dp))
                .border(1.dp, Gold, RoundedCornerShape(8.dp)),
        ) {
            Canvas(Modifier.fillMaxSize()) {
                val cx = size.width / 2f
                val headCy = size.height * 0.58f
                val headR = size.width * 0.34f

                drawCircle(
                    brush = Brush.radialGradient(
                        colors = listOf(Gold.copy(alpha = 0.45f), Color.Transparent),
                        center = Offset(cx, headCy),
                        radius = headR * 2.2f,
                    ),
                    radius = headR * 2.2f,
                    center = Offset(cx, headCy),
                )

                drawCircle(Burgundy, headR, Offset(cx, headCy))
                drawCircle(Gold, headR, Offset(cx, headCy), style = Stroke(1.5f))

                rotate(spec.tilt, pivot = Offset(cx, headCy)) {
                    val eyeY = headCy - headR * 0.25f
                    val eyeDx = headR * 0.4f
                    drawEye(spec.eye, spec.eyeColor, cx - eyeDx, eyeY)
                    drawEye(spec.eye, spec.eyeColor, cx + eyeDx, eyeY)
                    drawMouth(spec.mouth, cx, headCy + headR * 0.4f, headR * 0.5f)
                }
            }

            Image(
                bitmap = hat,
                contentDescription = null,
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .padding(top = 2.dp)
                    .size(40.dp),
            )
        }
        Text(
            "JOKER",
            color = CreamText,
            fontSize = 7.sp,
            letterSpacing = 2.sp,
        )
    }
}

private fun DrawScope.drawEye(shape: EyeShape, color: Color, cx: Float, cy: Float) {
    when (shape) {
        EyeShape.CLOSED -> {
            val p = Path().apply {
                moveTo(cx - 5f, cy)
                quadraticBezierTo(cx, cy - 3f, cx + 5f, cy)
            }
            drawPath(p, color, style = Stroke(1.8f, cap = StrokeCap.Round))
        }
        else -> {
            val hw = when (shape) {
                EyeShape.WIDE -> 4f
                EyeShape.NARROW -> 4f
                else -> 3f
            }
            val hh = when (shape) {
                EyeShape.WIDE -> 5f
                EyeShape.NARROW -> 1.5f
                else -> 4f
            }
            val p = Path().apply {
                moveTo(cx, cy - hh)
                lineTo(cx - hw, cy)
                lineTo(cx, cy + hh)
                lineTo(cx + hw, cy)
                close()
            }
            drawPath(p, color)
        }
    }
}

private fun DrawScope.drawMouth(shape: MouthShape, cx: Float, cy: Float, hw: Float) {
    when (shape) {
        MouthShape.LINE -> drawLine(
            CreamText,
            Offset(cx - hw, cy),
            Offset(cx + hw, cy),
            strokeWidth = 1.5f,
            cap = StrokeCap.Round,
        )
        MouthShape.SMIRK -> {
            val p = Path().apply {
                moveTo(cx - hw, cy)
                quadraticBezierTo(cx, cy + 4f, cx + hw, cy - 3f)
            }
            drawPath(p, CreamText, style = Stroke(1.8f, cap = StrokeCap.Round))
        }
        MouthShape.GRIN -> {
            val p = Path().apply {
                moveTo(cx - hw, cy - 1f)
                quadraticBezierTo(cx, cy + 7f, cx + hw, cy - 1f)
            }
            drawPath(p, CreamText, style = Stroke(2f, cap = StrokeCap.Round))
        }
        MouthShape.OPEN -> {
            drawOval(
                CreamText,
                topLeft = Offset(cx - hw, cy - 3f),
                size = Size(hw * 2f, 8f),
            )
        }
        MouthShape.FROWN -> {
            val p = Path().apply {
                moveTo(cx - hw, cy + 2f)
                quadraticBezierTo(cx, cy - 5f, cx + hw, cy + 2f)
            }
            drawPath(p, CreamText, style = Stroke(1.8f, cap = StrokeCap.Round))
        }
    }
}

@Composable
private fun QuoteBar(quote: String, modifier: Modifier) {
    var shown by remember { mutableStateOf("") }
    LaunchedEffect(quote) {
        shown = ""
        for (i in 1..quote.length) {
            shown = quote.substring(0, i)
            delay(20)
        }
    }
    val shape = RoundedCornerShape(3.dp, 8.dp, 8.dp, 3.dp)
    Row(
        modifier
            .background(
                Brush.verticalGradient(
                    listOf(DeepStage.copy(alpha = 0.92f), Color(0xEB0A0408)),
                ),
                shape,
            )
            .border(1.dp, MarqueeDim, shape),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(
            Modifier
                .width(3.dp)
                .height(34.dp)
                .background(Gold),
        )
        Text(
            "“",
            color = Gold,
            fontSize = 16.sp,
            modifier = Modifier.padding(start = 8.dp),
        )
        Spacer(Modifier.width(6.dp))
        val cursor = if (shown.length < quote.length) "▌" else ""
        Text(
            shown + cursor,
            color = CreamText,
            fontSize = 12.sp,
            fontStyle = FontStyle.Italic,
            fontFamily = FontFamily.Serif,
            textAlign = TextAlign.Start,
            modifier = Modifier.padding(end = 10.dp, top = 6.dp, bottom = 6.dp),
        )
    }
}
