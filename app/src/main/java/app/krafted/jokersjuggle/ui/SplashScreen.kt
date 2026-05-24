package app.krafted.jokersjuggle.ui

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.*
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
import app.krafted.jokersjuggle.ui.theme.DMSerifDisplay
import app.krafted.jokersjuggle.ui.theme.Gold
import app.krafted.jokersjuggle.ui.theme.SpaceGrotesk
import app.krafted.jokersjuggle.ui.theme.StageDark
import androidx.compose.foundation.border
import app.krafted.jokersjuggle.ui.theme.CreamText
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(onNavigateToHome: () -> Unit) {
    var phase by remember { mutableStateOf(0) }

    LaunchedEffect(Unit) {
        delay(800)
        phase = 1
        delay(1400)
        phase = 2
        delay(1400)
        onNavigateToHome()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .clickable { onNavigateToHome() }
    ) {
        StageBackdrop {
            // 1. Falling Demo Objects
            val fallingItems = listOf(
                R.drawable.jok021_sym_1 to 0.15f,
                R.drawable.jok021_sym_2 to 0.30f,
                R.drawable.jok021_sym_3 to 0.45f,
                R.drawable.jok021_sym_5 to 0.60f,
                R.drawable.jok021_sym_6 to 0.75f
            )

            fallingItems.forEachIndexed { index, (drawableId, leftPercent) ->
                val targetYPercent = if (phase >= 1) 0.50f + (index * 0.05f) else -0.1f
                val animatedY by animateFloatAsState(
                    targetValue = targetYPercent,
                    animationSpec = tween(
                        durationMillis = 1400,
                        delayMillis = index * 120,
                        easing = FastOutSlowInEasing
                    ),
                    label = "falling_item_$index"
                )

                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .graphicsLayer {
                            translationX = size.width * (leftPercent - 0.5f)
                            translationY = size.height * (animatedY - 0.5f)
                            rotationZ = index * 30f
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        painter = painterResource(id = drawableId),
                        contentDescription = null,
                        modifier = Modifier.size(48.dp)
                    )
                }
            }

            // 2. Glove Catches (Slide in from bottom-center/sides)
            val leftGloveOffset by animateDpAsState(
                targetValue = if (phase >= 2) 0.dp else 200.dp,
                animationSpec = tween(600, easing = EaseOutBack),
                label = "left_glove"
            )
            val rightGloveOffset by animateDpAsState(
                targetValue = if (phase >= 2) 0.dp else 200.dp,
                animationSpec = tween(600, easing = EaseOutBack),
                label = "right_glove"
            )

            if (phase >= 2) {
                // Left hand
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .graphicsLayer {
                            translationX = -size.width * 0.22f
                            translationY = size.height * 0.16f + leftGloveOffset.toPx()
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.jok021_glove),
                        contentDescription = null,
                        modifier = Modifier
                            .size(72.dp)
                            .graphicsLayer {
                                rotationZ = 10f
                            }
                    )
                }

                // Right hand (Flipped horizontally)
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .graphicsLayer {
                            translationX = size.width * 0.22f
                            translationY = size.height * 0.16f + rightGloveOffset.toPx()
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.jok021_glove),
                        contentDescription = null,
                        modifier = Modifier
                            .size(72.dp)
                            .graphicsLayer {
                                rotationZ = -10f
                                scaleX = -1f
                            }
                    )
                }
            }

            // 3. Title Reveal Card
            val titleAlpha by animateFloatAsState(
                targetValue = if (phase >= 1) 1f else 0f,
                animationSpec = tween(800),
                label = "title_alpha"
            )
            val titleScale by animateFloatAsState(
                targetValue = if (phase >= 1) 1f else 0.7f,
                animationSpec = spring(dampingRatio = 0.64f, stiffness = Spring.StiffnessLow),
                label = "title_scale"
            )

            // Title Floating effect
            val infiniteTransition = rememberInfiniteTransition(label = "title_float")
            val floatOffset by infiniteTransition.animateFloat(
                initialValue = -8f,
                targetValue = 8f,
                animationSpec = infiniteRepeatable(
                    animation = tween(1200, easing = EaseInOutSine),
                    repeatMode = RepeatMode.Reverse
                ),
                label = "hat_float"
            )

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp)
                    .align(Alignment.TopCenter)
                    .offset(y = 120.dp)
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
                    // Top marquee bulbs
                    MarqueeBulbs(count = 14, modifier = Modifier.padding(bottom = 12.dp))

                    // Floating Hat
                    Image(
                        painter = painterResource(id = R.drawable.jok021_sym_4),
                        contentDescription = "Joker Hat",
                        contentScale = ContentScale.Fit,
                        modifier = Modifier
                            .size(110.dp)
                            .offset(y = floatOffset.dp)
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

                    // Bottom marquee bulbs
                    MarqueeBulbs(count = 14, modifier = Modifier.padding(top = 16.dp))
                }
            }
        }
    }
}
