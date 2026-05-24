package app.krafted.jokersjuggle.ui.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.scale
import androidx.compose.ui.graphics.drawscope.withTransform
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import app.krafted.jokersjuggle.ui.theme.Burgundy
import app.krafted.jokersjuggle.ui.theme.CreamText
import app.krafted.jokersjuggle.ui.theme.DMSerifDisplay
import app.krafted.jokersjuggle.ui.theme.DeepStage
import app.krafted.jokersjuggle.ui.theme.Gold
import app.krafted.jokersjuggle.ui.theme.MarqueeDim
import app.krafted.jokersjuggle.ui.theme.SpaceGrotesk
import app.krafted.jokersjuggle.ui.theme.StageDark
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.foundation.Image
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.text.TextStyle
import app.krafted.jokersjuggle.R
import app.krafted.jokersjuggle.viewmodel.JokerExpression
import kotlinx.coroutines.delay
import kotlin.math.PI
import kotlin.math.sin

@Composable
fun GoldFlourish(
    modifier: Modifier = Modifier,
    width: Dp = 200.dp,
    color: Color = Gold,
    flip: Boolean = false
) {
    val height = width * 0.1f
    Canvas(
        modifier = modifier
            .width(width)
            .height(height)
    ) {
        val w = size.width
        val h = size.height
        val scaleX = w / 200f
        val scaleY = h / 20f

        withTransform({
            if (flip) {
                rotate(180f)
            }
        }) {
            drawLine(
                color = color,
                start = Offset(20f * scaleX, 10f * scaleY),
                end = Offset(80f * scaleX, 10f * scaleY),
                strokeWidth = 1f * scaleX,
                cap = StrokeCap.Round
            )
            drawLine(
                color = color,
                start = Offset(120f * scaleX, 10f * scaleY),
                end = Offset(180f * scaleX, 10f * scaleY),
                strokeWidth = 1f * scaleX,
                cap = StrokeCap.Round
            )

            val path1 = Path().apply {
                moveTo(80f * scaleX, 10f * scaleY)
                quadraticBezierTo(88f * scaleX, 4f * scaleY, 96f * scaleX, 10f * scaleY)
                quadraticBezierTo(104f * scaleX, 16f * scaleY, 100f * scaleX, 10f * scaleY)
                quadraticBezierTo(96f * scaleX, 4f * scaleY, 92f * scaleX, 10f * scaleY)
            }
            drawPath(path = path1, color = color, style = Stroke(width = 1f * scaleX, cap = StrokeCap.Round))

            val path2 = Path().apply {
                moveTo(120f * scaleX, 10f * scaleY)
                quadraticBezierTo(112f * scaleX, 4f * scaleY, 104f * scaleX, 10f * scaleY)
                quadraticBezierTo(96f * scaleX, 16f * scaleY, 100f * scaleX, 10f * scaleY)
                quadraticBezierTo(104f * scaleX, 4f * scaleY, 108f * scaleX, 10f * scaleY)
            }
            drawPath(path = path2, color = color, style = Stroke(width = 1f * scaleX, cap = StrokeCap.Round))

            drawCircle(color = color, radius = 2.5f * scaleX, center = Offset(100f * scaleX, 10f * scaleY))
            drawCircle(color = color, radius = 5f * scaleX, center = Offset(100f * scaleX, 10f * scaleY), style = Stroke(width = 1f * scaleX))

            drawLine(color = color, start = Offset(20f * scaleX, 10f * scaleY), end = Offset(14f * scaleX, 6f * scaleY), strokeWidth = 1f * scaleX, cap = StrokeCap.Round)
            drawLine(color = color, start = Offset(20f * scaleX, 10f * scaleY), end = Offset(14f * scaleX, 14f * scaleY), strokeWidth = 1f * scaleX, cap = StrokeCap.Round)
            drawCircle(color = color, radius = 1.5f * scaleX, center = Offset(14f * scaleX, 10f * scaleY))

            drawLine(color = color, start = Offset(180f * scaleX, 10f * scaleY), end = Offset(186f * scaleX, 6f * scaleY), strokeWidth = 1f * scaleX, cap = StrokeCap.Round)
            drawLine(color = color, start = Offset(180f * scaleX, 10f * scaleY), end = Offset(186f * scaleX, 14f * scaleY), strokeWidth = 1f * scaleX, cap = StrokeCap.Round)
            drawCircle(color = color, radius = 1.5f * scaleX, center = Offset(186f * scaleX, 10f * scaleY))
        }
    }
}

@Composable
fun MarqueeBulbs(
    count: Int = 14,
    color: Color = Color(0xFFFFD860),
    modifier: Modifier = Modifier
) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier.fillMaxWidth()
    ) {
        val infiniteTransition = rememberInfiniteTransition(label = "bulbs")
        for (i in 0 until count) {
            val delayMillis = (i * 70)
            val blinkDuration = 1200 + (i % 3) * 400
            
            val alpha by infiniteTransition.animateFloat(
                initialValue = 0.3f,
                targetValue = 1.0f,
                animationSpec = infiniteRepeatable(
                    animation = tween(durationMillis = blinkDuration, easing = LinearEasing),
                    repeatMode = RepeatMode.Reverse,
                    initialStartOffset = StartOffset(delayMillis)
                ),
                label = "bulb_blink_$i"
            )

            val glowRadius by infiniteTransition.animateFloat(
                initialValue = 1.dp.value,
                targetValue = 5.dp.value,
                animationSpec = infiniteRepeatable(
                    animation = tween(durationMillis = blinkDuration, easing = LinearEasing),
                    repeatMode = RepeatMode.Reverse,
                    initialStartOffset = StartOffset(delayMillis)
                ),
                label = "bulb_glow_$i"
            )

            Box(
                modifier = Modifier
                    .size(6.dp)
                    .drawBehind {
                        drawCircle(
                            brush = Brush.radialGradient(
                                colors = listOf(color.copy(alpha = alpha), Color(0xFFB88018).copy(alpha = alpha)),
                                center = center,
                                radius = size.minDimension / 2
                            )
                        )
                        drawCircle(
                            color = color.copy(alpha = alpha * 0.4f),
                            radius = (size.minDimension / 2) + glowRadius
                        )
                    }
            )
        }
    }
}

@Composable
fun PlaybillCard(
    modifier: Modifier = Modifier,
    accent: Color = Gold,
    dark: Boolean = true,
    content: @Composable BoxScope.() -> Unit
) {
    val bgBrush = if (dark) {
        Brush.verticalGradient(
            colors = listOf(Color(0xEB1A0A10), Color(0xEB0E0408))
        )
    } else {
        Brush.verticalGradient(
            colors = listOf(Color(0xF2F4E9D8), Color(0xF2D8C4A8))
        )
    }

    Box(
        modifier = modifier
            .shadowCard(accent = accent, bgBrush = bgBrush)
            .padding(14.dp)
    ) {
        Canvas(modifier = Modifier.matchParentSize()) {
            val w = size.width
            val h = size.height

            val corners = listOf(
                Offset(0f, 0f) to RotateFlip(0f, 1f, 1f),
                Offset(w, 0f) to RotateFlip(90f, -1f, 1f),
                Offset(w, h) to RotateFlip(180f, -1f, -1f),
                Offset(0f, h) to RotateFlip(270f, 1f, -1f)
            )

            corners.forEach { (offset, rf) ->
                withTransform({
                    translate(offset.x, offset.y)
                    rotate(rf.angle)
                }) {
                    val path = Path().apply {
                        moveTo(0f, 0f)
                        lineTo(35f, 0f)
                        lineTo(35f, 5f)
                        lineTo(10f, 5f)
                        lineTo(10f, 10f)
                        lineTo(5f, 10f)
                        lineTo(5f, 35f)
                        lineTo(0f, 35f)
                        close()
                    }
                    drawPath(path = path, color = accent, alpha = 0.7f)
                    drawCircle(color = accent, radius = 2.5f, center = Offset(8f, 8f))
                }
            }
        }

        Box(
            modifier = Modifier.padding(8.dp),
            content = content
        )
    }
}

private data class RotateFlip(val angle: Float, val scaleX: Float, val scaleY: Float)

private fun Modifier.shadowCard(accent: Color, bgBrush: Brush): Modifier = this.drawBehind {
    val r = 8.dp.toPx()
    drawRoundRect(
        brush = bgBrush,
        cornerRadius = CornerRadius(r, r)
    )
    drawRoundRect(
        color = accent,
        cornerRadius = CornerRadius(r, r),
        style = Stroke(width = 1.dp.toPx())
    )
    drawRoundRect(
        color = Color.Black.copy(alpha = 0.3f),
        cornerRadius = CornerRadius(r - 4f, r - 4f),
        topLeft = Offset(4f, 4f),
        size = Size(size.width - 8f, size.height - 8f),
        style = Stroke(width = 1.dp.toPx())
    )
}

@Composable
fun SpotlightCone(
    modifier: Modifier = Modifier,
    leftPercent: Float = 0.5f,
    tiltDegrees: Float = 0f,
    intensity: Float = 0.2f,
    color: Color = Color(0xFFFFD860)
) {
    Canvas(modifier = modifier.fillMaxSize()) {
        val w = size.width
        val h = size.height

        val spotlightWidth = 240.dp.toPx()
        val spotlightHeight = 600.dp.toPx()
        val startX = w * leftPercent
        val translateY = -60.dp.toPx()

        withTransform({
            translate(startX, translateY)
            rotate(tiltDegrees, pivot = Offset(0f, 0f))
        }) {
            val conePath = Path().apply {
                moveTo(-spotlightWidth * 0.05f, 0f)
                lineTo(spotlightWidth * 0.05f, 0f)
                lineTo(spotlightWidth * 0.4f, spotlightHeight)
                lineTo(-spotlightWidth * 0.4f, spotlightHeight)
                close()
            }

            drawPath(
                path = conePath,
                brush = Brush.verticalGradient(
                    colors = listOf(
                        color.copy(alpha = intensity * 1.5f),
                        color.copy(alpha = intensity * 0.4f),
                        Color.Transparent
                    ),
                    startY = 0f,
                    endY = spotlightHeight
                )
            )
        }
    }
}

@Composable
fun DustParticles(
    count: Int = 18,
    modifier: Modifier = Modifier
) {
    val infiniteTransition = rememberInfiniteTransition(label = "dust")
    
    val phase by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 2f * PI.toFloat(),
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 8000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "dust_phase"
    )

    Canvas(modifier = modifier.fillMaxSize()) {
        val w = size.width
        val h = size.height

        for (i in 0 until count) {
            val xSeed = (i * 17 + 7) % 100
            val ySeed = (i * 29 + 13) % 100
            val sizeRadius = (1 + (i % 3)).dp.toPx()
            
            val speedFactor = 1f + (i % 5) * 0.2f
            val driftX = sin(phase * speedFactor + i) * 15.dp.toPx()
            val driftY = sin(phase * speedFactor * 0.8f + i * 2) * 20.dp.toPx()

            val posX = (w * (xSeed / 100f) + driftX).coerceIn(0f, w)
            val posY = (h * (ySeed / 100f) + driftY).coerceIn(0f, h)

            drawCircle(
                color = Gold.copy(alpha = 0.45f),
                radius = sizeRadius,
                center = Offset(posX, posY)
            )
            drawCircle(
                color = Gold.copy(alpha = 0.15f),
                radius = sizeRadius * 2.5f,
                center = Offset(posX, posY)
            )
        }
    }
}

@Composable
fun CurtainDrapes(
    isOpen: Float,
    modifier: Modifier = Modifier
) {
    val animatedOpen by animateFloatAsState(
        targetValue = isOpen,
        animationSpec = tween(durationMillis = 800, easing = FastOutSlowInEasing),
        label = "curtain_open"
    )

    BoxWithConstraints(modifier = modifier.fillMaxSize()) {
        val w = constraints.maxWidth.toFloat()
        val h = constraints.maxHeight.toFloat()
        val density = LocalDensity.current

        Canvas(modifier = Modifier.fillMaxSize()) {
            val drapeMaxW = w * 0.5f
            val openOverlap = 0.dp.toPx()
            val curtainW = drapeMaxW * (1f - animatedOpen) + (animatedOpen * openOverlap)

            drawRect(
                brush = Brush.verticalGradient(
                    colors = listOf(Color(0xFF3A0810), Color(0xFF7A1521), Color(0xFF3A0810))
                ),
                topLeft = Offset(0f, 0f),
                size = Size(curtainW, h)
            )
            drawRect(
                brush = Brush.horizontalGradient(
                    colors = listOf(
                        Color.Black.copy(alpha = 0.5f),
                        Color.Transparent,
                        Color.Black.copy(alpha = 0.7f)
                    )
                ),
                topLeft = Offset(0f, 0f),
                size = Size(curtainW, h)
            )
            drawLine(
                color = Color(0xFF2A0408),
                start = Offset(curtainW - 2.dp.toPx(), 0f),
                end = Offset(curtainW - 2.dp.toPx(), h),
                strokeWidth = 4.dp.toPx()
            )

            val rightCurtainLeft = w - curtainW
            drawRect(
                brush = Brush.verticalGradient(
                    colors = listOf(Color(0xFF3A0810), Color(0xFF7A1521), Color(0xFF3A0810))
                ),
                topLeft = Offset(rightCurtainLeft, 0f),
                size = Size(curtainW, h)
            )
            drawRect(
                brush = Brush.horizontalGradient(
                    colors = listOf(
                        Color.Black.copy(alpha = 0.7f),
                        Color.Transparent,
                        Color.Black.copy(alpha = 0.5f)
                    )
                ),
                topLeft = Offset(rightCurtainLeft, 0f),
                size = Size(curtainW, h)
            )
            drawLine(
                color = Color(0xFF2A0408),
                start = Offset(rightCurtainLeft + 2.dp.toPx(), 0f),
                end = Offset(rightCurtainLeft + 2.dp.toPx(), h),
                strokeWidth = 4.dp.toPx()
            )

            val valanceH = 34.dp.toPx()
            drawRect(
                brush = Brush.verticalGradient(
                    colors = listOf(Color(0xFF8A1828), Color(0xFF5A0E18))
                ),
                topLeft = Offset(0f, 0f),
                size = Size(w, valanceH)
            )
            drawLine(
                brush = Brush.horizontalGradient(
                    colors = listOf(Color.Transparent, Gold, Color(0xFFFFD860), Gold, Color.Transparent)
                ),
                start = Offset(0f, valanceH),
                end = Offset(w, valanceH),
                strokeWidth = 2.dp.toPx()
            )

            val scallopW = 24.dp.toPx()
            val scallopH = 10.dp.toPx()
            val scallopCount = (w / scallopW).toInt() + 1
            for (i in 0 until scallopCount) {
                val startX = i * scallopW
                val scallopPath = Path().apply {
                    moveTo(startX, valanceH)
                    quadraticBezierTo(
                        startX + scallopW / 2, valanceH + scallopH,
                        startX + scallopW, valanceH
                    )
                }
                drawPath(path = scallopPath, color = Color(0xFF5A0E18))
                
                drawCircle(
                    color = Gold,
                    radius = 2.dp.toPx(),
                    center = Offset(startX + scallopW / 2, valanceH + scallopH + 2.dp.toPx())
                )
            }
        }

        if (animatedOpen > 0.4f && animatedOpen < 0.95f) {
            val openOverlap = with(density) { 0.dp.toPx() }
            val tasselOffsetLeftPx = (w * 0.5f) * (1f - animatedOpen) + (animatedOpen * openOverlap)
            val tasselOffsetLeftDp = with(density) { (tasselOffsetLeftPx - 6.dp.toPx()).coerceAtLeast(0f).toDp() }

            TasselWidget(
                modifier = Modifier
                    .align(Alignment.CenterStart)
                    .offset(x = tasselOffsetLeftDp, y = 0.dp)
            )

            TasselWidget(
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .offset(x = -tasselOffsetLeftDp, y = 0.dp)
            )
        }
    }
}


@Composable
private fun TasselWidget(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .width(14.dp)
            .height(60.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .width(4.dp)
                .height(30.dp)
                .background(
                    Brush.verticalGradient(
                        colors = listOf(Gold, Color(0xFF8A5008))
                    ),
                    shape = RoundedCornerShape(2.dp)
                )
        )
        Box(
            modifier = Modifier
                .size(14.dp)
                .background(
                    Brush.radialGradient(
                        colors = listOf(Color(0xFFFFD860), Color(0xFF8A5008)),
                        radius = 20f
                    ),
                    shape = RoundedCornerShape(7.dp)
                )
                .border(0.5.dp, MarqueeDim, RoundedCornerShape(7.dp))
        )
        Box(
            modifier = Modifier
                .width(6.dp)
                .height(14.dp)
                .drawBehind {
                    val w = size.width
                    val h = size.height
                    drawLine(Color(0xFFB88018), Offset(0f, 0f), Offset(0f, h), strokeWidth = 1.dp.toPx())
                    drawLine(Color(0xFFB88018), Offset(w/2, 0f), Offset(w/2, h), strokeWidth = 1.dp.toPx())
                    drawLine(Color(0xFFB88018), Offset(w, 0f), Offset(w, h), strokeWidth = 1.dp.toPx())
                }
        )
    }
}

@Composable
fun PrimaryButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    accent: ButtonAccent = ButtonAccent.GOLD,
    enabled: Boolean = true,
    content: @Composable RowScope.() -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    
    val translationY by animateDpAsState(
        targetValue = if (isPressed) 3.dp else 0.dp,
        animationSpec = spring(stiffness = Spring.StiffnessHigh),
        label = "press_translation"
    )

    val shadowHeight = if (isPressed) 2.dp else 5.dp

    val colors = when (accent) {
        ButtonAccent.GOLD -> ButtonColors(
            bgBrush = Brush.verticalGradient(listOf(Color(0xFFFFD860), Gold, Color(0xFFB88018))),
            textColor = Color(0xFF2A0408),
            borderColor = Color(0xFF5A3A08),
            shadowColor = Color(0xFF5A3A08),
            highlightColor = Color(0xFFFFF5B0)
        )
        ButtonAccent.RED -> ButtonColors(
            bgBrush = Brush.verticalGradient(listOf(Color(0xFF8A1828), Color(0xFF5A0E18), Color(0xFF2A0408))),
            textColor = CreamText,
            borderColor = Gold,
            shadowColor = Color(0xFF1A0408),
            highlightColor = Color(0xFFC91A1A)
        )
    }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(56.dp + 5.dp)
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                enabled = enabled,
                onClick = onClick
            )
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .align(Alignment.BottomCenter)
                .background(colors.shadowColor, shape = RoundedCornerShape(6.dp))
        )

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .offset(y = translationY)
                .background(colors.bgBrush, shape = RoundedCornerShape(6.dp))
                .border(2.dp, colors.borderColor, shape = RoundedCornerShape(6.dp))
                .drawBehind {
                    drawLine(
                        colors.highlightColor,
                        Offset(x = size.width * 0.1f, y = 1.dp.toPx()),
                        Offset(x = size.width * 0.9f, y = 1.dp.toPx()),
                        strokeWidth = 1.dp.toPx()
                    )
                },
            contentAlignment = Alignment.Center
        ) {
            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,
                content = content
            )
        }
    }
}

enum class ButtonAccent { GOLD, RED }

private data class ButtonColors(
    val bgBrush: Brush,
    val textColor: Color,
    val borderColor: Color,
    val shadowColor: Color,
    val highlightColor: Color
)

@Composable
fun StageBackdrop(
    modifier: Modifier = Modifier,
    withSpots: Boolean = true,
    withDust: Boolean = true,
    content: @Composable BoxScope.() -> Unit
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(StageDark)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.radialGradient(
                        colors = listOf(Color.Transparent, Color.Black.copy(alpha = 0.7f)),
                        radius = 2000f
                    )
                )
        )

        if (withSpots) {
            SpotlightCone(leftPercent = 0.2f, tiltDegrees = 15f, intensity = 0.18f)
            SpotlightCone(leftPercent = 0.8f, tiltDegrees = -15f, intensity = 0.18f)
            SpotlightCone(leftPercent = 0.5f, tiltDegrees = 0f, intensity = 0.1f)
        }

        Canvas(modifier = Modifier.fillMaxSize()) {
            val paint = Paint().asFrameworkPaint().apply {
                color = Gold.toArgb()
                alpha = (255 * 0.035f).toInt()
                textSize = 36f
            }
            val suits = listOf("♠", "♥", "♣", "♦")
            val spacing = 80.dp.toPx()
            for (y in 0 until (size.height / spacing).toInt() + 1) {
                for (x in 0 until (size.width / spacing).toInt() + 1) {
                    val suit = suits[(x + y) % 4]
                    val drawX = x * spacing + (if (y % 2 == 1) spacing / 2 else 0f)
                    val drawY = y * spacing
                    drawContext.canvas.nativeCanvas.drawText(suit, drawX, drawY, paint)
                }
            }
        }

        if (withDust) {
            DustParticles(count = 18)
        }

        content()
    }
}

@Composable
fun Typewriter(
    text: String,
    modifier: Modifier = Modifier,
    style: TextStyle = TextStyle.Default,
    color: Color = CreamText,
    onComplete: () -> Unit = {}
) {
    var shownText by remember { mutableStateOf("") }
    
    LaunchedEffect(text) {
        shownText = ""
        for (i in 1..text.length) {
            shownText = text.substring(0, i)
            delay(20)
        }
        onComplete()
    }

    val cursor = if (shownText.length < text.length) "▌" else ""
    Text(
        text = shownText + cursor,
        color = color,
        style = style,
        modifier = modifier
    )
}

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
fun JokerPortrait(
    expression: JokerExpression,
    portraitSize: Dp,
    modifier: Modifier = Modifier
) {
    val glowColor = when (expression) {
        JokerExpression.SINISTER, JokerExpression.TAUNTING, JokerExpression.UNHINGED -> Color(0xFFC91A1A)
        JokerExpression.LAUGHING, JokerExpression.GLEEFUL, JokerExpression.ECSTATIC -> Color(0xFFFFD860)
        else -> Gold
    }

    Box(
        modifier = modifier
            .size(portraitSize),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .size(portraitSize * 1.35f)
                .background(
                    Brush.radialGradient(
                        colors = listOf(
                            glowColor.copy(alpha = 0.4f),
                            Burgundy.copy(alpha = 0.15f),
                            Color.Transparent
                        )
                    )
                )
        )

        Image(
            painter = painterResource(id = R.drawable.joker_avatar),
            contentDescription = "Joker Portrait - ${expression.name}",
            modifier = Modifier
                .size(portraitSize)
                .clip(RoundedCornerShape(8.dp))
                .border(1.5.dp, Gold, RoundedCornerShape(8.dp))
        )
    }
}

