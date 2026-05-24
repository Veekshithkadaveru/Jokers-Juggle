package app.krafted.jokersjuggle.ui

import androidx.compose.animation.core.EaseInOutSine
import androidx.compose.animation.core.EaseOutBack
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
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
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import app.krafted.jokersjuggle.R
import app.krafted.jokersjuggle.ui.components.GoldFlourish
import app.krafted.jokersjuggle.ui.components.MarqueeBulbs
import app.krafted.jokersjuggle.ui.components.StageBackdrop
import app.krafted.jokersjuggle.ui.theme.CreamText
import app.krafted.jokersjuggle.ui.theme.DMSerifDisplay
import app.krafted.jokersjuggle.ui.theme.Gold
import app.krafted.jokersjuggle.ui.theme.SpaceGrotesk
import kotlinx.coroutines.delay
import kotlin.math.PI
import kotlin.math.sin

@Composable
fun SplashScreen(onNavigateToHome: () -> Unit) {
    var phase by remember { mutableStateOf(0) }

    LaunchedEffect(Unit) {
        delay(400)
        phase = 1
        delay(600)
        phase = 2
        delay(3000)
        onNavigateToHome()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .clickable { onNavigateToHome() }
    ) {
        StageBackdrop {

            val juggleItems = listOf(
                R.drawable.jok021_sym_1,
                R.drawable.jok021_sym_2,
                R.drawable.jok021_sym_3,
                R.drawable.jok021_sym_5,
                R.drawable.jok021_sym_6,
                R.drawable.jok021_sym_7
            )

            val totalItems = juggleItems.size

            val cycleDurationMs = 1800

            val infiniteTransition = rememberInfiniteTransition(label = "juggle")

            juggleItems.forEachIndexed { index, drawableId ->

                val phaseOffsetMs = (cycleDurationMs * index) / totalItems

                val rawProgress by infiniteTransition.animateFloat(
                    initialValue = 0f,
                    targetValue = 1f,
                    animationSpec = infiniteRepeatable(
                        animation = tween(
                            durationMillis = cycleDurationMs,
                            easing = LinearEasing
                        ),
                        repeatMode = RepeatMode.Restart
                    ),
                    label = "juggle_progress_$index"
                )


                val offsetFraction = phaseOffsetMs.toFloat() / cycleDurationMs.toFloat()
                val progress = (rawProgress + offsetFraction) % 1f

                val isFirstHalf = progress < 0.5f
                val halfProgress = if (isFirstHalf) progress / 0.5f else (progress - 0.5f) / 0.5f

                val leftHandX = -0.20f
                val rightHandX = 0.20f

                val xFraction = if (isFirstHalf) {

                    leftHandX + (rightHandX - leftHandX) * halfProgress
                } else {

                    rightHandX + (leftHandX - rightHandX) * halfProgress
                }

                val arcHeight = sin(PI.toFloat() * halfProgress)

                val handY = 0.22f
                val peakHeight = 0.40f

                val yFraction = handY - (arcHeight * peakHeight)

                val rotation = progress * 720f * if (index % 2 == 0) 1f else -1f

                val itemScale = 0.85f + 0.3f * arcHeight

                val juggleAlpha by animateFloatAsState(
                    targetValue = if (phase >= 1) 1f else 0f,
                    animationSpec = tween(
                        durationMillis = 400,
                        delayMillis = index * 80
                    ),
                    label = "juggle_alpha_$index"
                )

                if (phase >= 1) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .graphicsLayer {
                                translationX = size.width * xFraction
                                translationY = size.height * yFraction
                                rotationZ = rotation
                                scaleX = itemScale
                                scaleY = itemScale
                                alpha = juggleAlpha
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Image(
                            painter = painterResource(id = drawableId),
                            contentDescription = null,
                            modifier = Modifier.size(52.dp)
                        )
                    }
                }
            }

            val leftGloveOffset by animateDpAsState(
                targetValue = if (phase >= 1) 0.dp else 300.dp,
                animationSpec = tween(700, easing = EaseOutBack),
                label = "left_glove"
            )

            val rightGloveOffset by animateDpAsState(
                targetValue = if (phase >= 1) 0.dp else 300.dp,
                animationSpec = tween(700, delayMillis = 100, easing = EaseOutBack),
                label = "right_glove"
            )

            val handBob by infiniteTransition.animateFloat(
                initialValue = -4f,
                targetValue = 4f,
                animationSpec = infiniteRepeatable(
                    animation = tween(450, easing = EaseInOutSine),
                    repeatMode = RepeatMode.Reverse
                ),
                label = "hand_bob"
            )

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .graphicsLayer {
                        translationX = -size.width * 0.22f
                        translationY = size.height * 0.28f + leftGloveOffset.toPx()
                    },
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.jok021_glove_throw),
                    contentDescription = "Left juggling hand",
                    modifier = Modifier
                        .size(100.dp)
                        .graphicsLayer {
                            translationY = handBob
                            rotationZ = 15f
                        }
                )
            }

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .graphicsLayer {
                        translationX = size.width * 0.22f
                        translationY = size.height * 0.28f + rightGloveOffset.toPx()
                    },
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.jok021_glove_throw),
                    contentDescription = "Right juggling hand",
                    modifier = Modifier
                        .size(100.dp)
                        .graphicsLayer {
                            translationY = -handBob
                            rotationZ = -15f
                            scaleX = -1f
                        }
                )
            }

            val titleAlpha by animateFloatAsState(
                targetValue = if (phase >= 2) 1f else 0f,
                animationSpec = tween(800),
                label = "title_alpha"
            )
            val titleScale by animateFloatAsState(
                targetValue = if (phase >= 2) 1f else 0.7f,
                animationSpec = spring(dampingRatio = 0.64f, stiffness = Spring.StiffnessLow),
                label = "title_scale"
            )

            val titleFloat by infiniteTransition.animateFloat(
                initialValue = -6f,
                targetValue = 6f,
                animationSpec = infiniteRepeatable(
                    animation = tween(1200, easing = EaseInOutSine),
                    repeatMode = RepeatMode.Reverse
                ),
                label = "title_float"
            )

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp)
                    .align(Alignment.TopCenter)
                    .offset(y = 100.dp)
                    .alpha(titleAlpha)
                    .scale(titleScale),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(8.dp))
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(Color(0xB33C0C14), Color(0xB314040A))
                            )
                        )
                        .border(1.dp, Gold, RoundedCornerShape(8.dp))
                        .padding(horizontal = 18.dp, vertical = 24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    MarqueeBulbs(count = 14, modifier = Modifier.padding(bottom = 12.dp))

                    Image(
                        painter = painterResource(id = R.drawable.jok021_sym_4),
                        contentDescription = "Joker Hat",
                        contentScale = ContentScale.Fit,
                        modifier = Modifier
                            .size(110.dp)
                            .offset(y = titleFloat.dp)
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "Joker's",
                        color = CreamText,
                        fontSize = 38.sp,
                        fontFamily = DMSerifDisplay,
                        textAlign = TextAlign.Center,
                        lineHeight = 38.sp
                    )
                    Text(
                        text = "Juggle",
                        color = Gold,
                        fontSize = 62.sp,
                        fontFamily = DMSerifDisplay,
                        fontStyle = androidx.compose.ui.text.font.FontStyle.Italic,
                        textAlign = TextAlign.Center,
                        lineHeight = 62.sp,
                        modifier = Modifier.offset(y = (-4).dp)
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        GoldFlourish(width = 60.dp)
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "TWO-THUMB CIRCUS",
                            color = Gold.copy(alpha = 0.8f),
                            fontFamily = SpaceGrotesk,
                            fontWeight = FontWeight.Bold,
                            fontSize = 9.sp,
                            letterSpacing = 2.sp,
                            textAlign = TextAlign.Center
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        GoldFlourish(width = 60.dp, flip = true)
                    }

                    MarqueeBulbs(count = 14, modifier = Modifier.padding(top = 16.dp))
                }
            }
        }
    }
}
