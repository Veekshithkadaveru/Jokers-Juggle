package app.krafted.jokersjuggle.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import app.krafted.jokersjuggle.ui.theme.Burgundy
import app.krafted.jokersjuggle.ui.theme.CreamText
import app.krafted.jokersjuggle.ui.theme.DeepStage
import app.krafted.jokersjuggle.ui.theme.Gold
import app.krafted.jokersjuggle.ui.theme.MarqueeDim
import app.krafted.jokersjuggle.ui.theme.StageDark
import app.krafted.jokersjuggle.viewmodel.GameUiState
import app.krafted.jokersjuggle.viewmodel.JokerExpression
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.unit.Dp
import kotlin.math.ceil

private val WarningOrange = Color(0xFFFF8050)
private val Frenzy = Color(0xFFFFD860)

@Composable
fun HudOverlay(state: GameUiState, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 10.dp, vertical = 6.dp)
    ) {
        ExcitementMeterRow(state.audienceExcitement)
        StatsBar(score = state.score, lives = state.lives, act = state.currentAct)
        TimerComboRow(state)
    }
}

@Composable
private fun ExcitementMeterRow(excitement: Float) {
    val fraction by animateFloatAsState(
        targetValue = (excitement / 100f).coerceIn(0f, 1f),
        animationSpec = tween(durationMillis = 300),
        label = "excitement"
    )
    val level = when {
        excitement < 25f -> "COLD"
        excitement < 50f -> "WARM"
        excitement < 75f -> "HOT"
        else -> "FRENZY"
    }
    val fillColor = when {
        excitement < 25f -> Color(0xFF5A6A8A)
        excitement < 50f -> Color(0xFFC8853A)
        excitement < 75f -> Color(0xFFFF8030)
        else -> Frenzy
    }
    val labelColor = if (excitement >= 75f) Frenzy else CreamText

    Row(verticalAlignment = Alignment.CenterVertically) {
        Text(text = "😐", fontSize = 13.sp)
        Box(
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 6.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(10.dp)
                    .clip(RoundedCornerShape(5.dp))
                    .background(Color.Black.copy(alpha = 0.6f))
                    .border(1.dp, MarqueeDim, RoundedCornerShape(5.dp))
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth(fraction)
                        .fillMaxHeight()
                        .background(fillColor)
                )
            }
            Text(
                text = level,
                color = labelColor,
                fontSize = 8.sp,
                fontWeight = FontWeight.SemiBold,
                letterSpacing = 1.sp,
                modifier = Modifier.align(Alignment.CenterEnd)
            )
        }
        Text(text = "🤩", fontSize = 13.sp)
    }
}

@Composable
private fun StatsBar(score: Int, lives: Int, act: Int) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 5.dp)
            .clip(RoundedCornerShape(4.dp))
            .background(
                Brush.verticalGradient(
                    listOf(DeepStage.copy(alpha = 0.85f), StageDark.copy(alpha = 0.85f))
                )
            )
            .border(1.dp, MarqueeDim, RoundedCornerShape(4.dp))
            .padding(horizontal = 10.dp, vertical = 6.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Lives(lives)
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = "SCORE",
                color = CreamText.copy(alpha = 0.8f),
                fontSize = 8.sp,
                letterSpacing = 2.sp
            )
            Text(
                text = score.toString(),
                color = CreamText,
                style = TextStyle(
                    fontSize = 22.sp,
                    fontStyle = FontStyle.Italic,
                    fontWeight = FontWeight.Medium
                )
            )
        }
        Text(
            text = "ACT $act/3",
            color = Gold,
            fontSize = 10.sp,
            fontWeight = FontWeight.SemiBold,
            letterSpacing = 1.sp,
            modifier = Modifier
                .clip(RoundedCornerShape(3.dp))
                .background(Color(0xFF1A0808))
                .border(1.dp, MarqueeDim, RoundedCornerShape(3.dp))
                .padding(horizontal = 8.dp, vertical = 4.dp)
        )
    }
}

@Composable
private fun Lives(lives: Int) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        for (i in 0 until 3) {
            HeartIcon(alive = i < lives, size = 18.dp)
        }
    }
}

@Composable
private fun HeartIcon(alive: Boolean, size: Dp) {
    val fill = if (alive) Burgundy else DeepStage
    val stroke = if (alive) Color(0xFFC91A1A) else MarqueeDim
    Box(
        modifier = Modifier
            .size(size)
            .drawBehind {
                val w = this.size.width
                val h = this.size.height
                // heart path scaled from the reference 24x24 viewBox
                val sx = w / 24f
                val sy = h / 24f
                val path = Path().apply {
                    moveTo(12f * sx, 21f * sy)
                    cubicTo(12f * sx, 21f * sy, 4f * sx, 14f * sy, 4f * sx, 9f * sy)
                    cubicTo(4f * sx, 6f * sy, 6f * sx, 4f * sy, 9f * sx, 4f * sy)
                    cubicTo(10.5f * sx, 4f * sy, 11.5f * sx, 4.6f * sy, 12f * sx, 5.5f * sy)
                    cubicTo(12.5f * sx, 4.6f * sy, 13.5f * sx, 4f * sy, 15f * sx, 4f * sy)
                    cubicTo(18f * sx, 4f * sy, 20f * sx, 6f * sy, 20f * sx, 9f * sy)
                    cubicTo(20f * sx, 14f * sy, 12f * sx, 21f * sy, 12f * sx, 21f * sy)
                    close()
                }
                drawPath(path = path, color = fill)
                drawPath(
                    path = path,
                    color = stroke,
                    style = androidx.compose.ui.graphics.drawscope.Stroke(width = 1f * sx)
                )
            }
    )
}

@Composable
private fun TimerComboRow(state: GameUiState) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 4.dp, start = 6.dp, end = 6.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        val warning = state.timeRemainingMs < 10_000L
        Text(
            text = formatTime(state.timeRemainingMs),
            color = if (warning) WarningOrange else CreamText,
            fontFamily = FontFamily.Monospace,
            fontSize = 12.sp
        )

        if (state.comboStreak >= 5) {
            val comboColor = when {
                state.comboMultiplier >= 5 -> Frenzy
                state.comboMultiplier >= 4 -> Color(0xFFFF5020)
                state.comboMultiplier >= 3 -> Color(0xFFFF9A30)
                else -> Frenzy
            }
            Text(
                text = "×${state.comboMultiplier}  ·  ${state.comboStreak} STREAK",
                color = comboColor,
                fontStyle = FontStyle.Italic,
                fontWeight = FontWeight.SemiBold,
                fontSize = 16.sp,
                letterSpacing = 1.sp
            )
        } else {
            Text(
                text = "· · · ·",
                color = MarqueeDim,
                fontSize = 10.sp,
                letterSpacing = 2.sp
            )
        }

        if (state.isMultiplierActive) {
            Text(
                text = "★ 2× · ${state.multiplierSecondsLeft}s",
                color = Frenzy,
                fontSize = 10.sp,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier
                    .clip(RoundedCornerShape(3.dp))
                    .background(Frenzy.copy(alpha = 0.15f))
                    .border(1.dp, Frenzy, RoundedCornerShape(3.dp))
                    .padding(horizontal = 6.dp, vertical = 2.dp)
            )
        } else {
            Box(modifier = Modifier.width(50.dp))
        }
    }
}

private fun formatTime(ms: Long): String {
    val totalSeconds = ceil(ms.coerceAtLeast(0L) / 1000.0).toInt()
    val minutes = totalSeconds / 60
    val seconds = totalSeconds % 60
    return "%02d:%02d".format(minutes, seconds)
}

@Preview
@Composable
private fun HudOverlayPreview() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(StageDark)
    ) {
        HudOverlay(
            state = GameUiState(
                score = 12450,
                lives = 2,
                currentAct = 2,
                timeRemainingMs = 8000L,
                comboStreak = 7,
                comboMultiplier = 3,
                audienceExcitement = 82f,
                isMultiplierActive = true,
                multiplierSecondsLeft = 4,
                jokerExpression = JokerExpression.GLEEFUL,
                jokerQuote = "Bravo!",
                controlsSwapped = false,
                screenAlpha = 1f
            )
        )
    }
}
