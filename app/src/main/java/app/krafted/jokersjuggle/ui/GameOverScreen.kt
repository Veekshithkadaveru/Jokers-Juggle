package app.krafted.jokersjuggle.ui

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import app.krafted.jokersjuggle.ui.components.*
import app.krafted.jokersjuggle.ui.theme.*
import app.krafted.jokersjuggle.viewmodel.JokerExpression

@Composable
fun GameOverScreen(
    score: Int,
    timeSurvivedSeconds: Int,
    maxObjectsReached: Int,
    onSaveAndReplay: (playerName: String) -> Unit,
    onSaveAndHome: (playerName: String) -> Unit
) {
    var curtainOpen by remember { mutableStateOf(1f) }
    var playerName by remember { mutableStateOf("") }
    val focusManager = LocalFocusManager.current
    val focusRequester = remember { FocusRequester() }

    LaunchedEffect(Unit) {
        curtainOpen = 0.12f
    }

    Box(modifier = Modifier.fillMaxSize()) {
        StageBackdrop {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 24.dp, vertical = 24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(48.dp))

                // Header section
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    GoldFlourish(width = 70.dp, color = Color(0xFFC91A1A))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "THE PERFORMANCE ENDS",
                        color = Gold,
                        fontSize = 10.sp,
                        fontFamily = SpaceGrotesk,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 4.sp
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    GoldFlourish(width = 70.dp, color = Color(0xFFC91A1A), flip = true)
                }

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Curtain.",
                    color = Color(0xFFC91A1A),
                    fontSize = 58.sp,
                    fontFamily = DMSerifDisplay,
                    fontStyle = FontStyle.Italic,
                    lineHeight = 58.sp
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Joker laughing centerpiece
                Box(
                    contentAlignment = Alignment.BottomCenter,
                    modifier = Modifier.height(160.dp)
                ) {
                    JokerPortrait(
                        expression = JokerExpression.LAUGHING,
                        portraitSize = 130.dp
                    )
                    Box(
                        modifier = Modifier
                            .width(180.dp)
                            .height(8.dp)
                            .background(
                                Brush.radialGradient(
                                    colors = listOf(Color(0xFFC91A1A).copy(alpha = 0.3f), Color.Transparent)
                                )
                            )
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Playbill Card with Stats
                PlaybillCard(
                    modifier = Modifier.fillMaxWidth(0.9f)
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = "FINAL SCORE",
                            color = Gold,
                            fontSize = 9.sp,
                            fontFamily = SpaceGrotesk,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 4.sp
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = score.toString(),
                            color = CreamText,
                            fontSize = 54.sp,
                            fontFamily = DMSerifDisplay,
                            fontStyle = FontStyle.Italic,
                            fontWeight = FontWeight.Bold,
                            lineHeight = 54.sp
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        // Separator
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(1.dp)
                                .background(Gold.copy(alpha = 0.2f))
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceEvenly,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text(
                                    text = "⏱ TIME",
                                    color = CreamText.copy(alpha = 0.6f),
                                    fontSize = 10.sp,
                                    fontFamily = SpaceGrotesk,
                                    fontWeight = FontWeight.SemiBold,
                                    letterSpacing = 1.sp
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = formatTime(timeSurvivedSeconds),
                                    color = CreamText,
                                    fontSize = 18.sp,
                                    fontFamily = SpaceGrotesk,
                                    fontWeight = FontWeight.Bold
                                )
                            }

                            // Vertical divider
                            Box(
                                modifier = Modifier
                                    .width(1.dp)
                                    .height(36.dp)
                                    .background(Gold.copy(alpha = 0.2f))
                            )

                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text(
                                    text = "🃏 MAX OBJECTS",
                                    color = CreamText.copy(alpha = 0.6f),
                                    fontSize = 10.sp,
                                    fontFamily = SpaceGrotesk,
                                    fontWeight = FontWeight.SemiBold,
                                    letterSpacing = 1.sp
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = maxObjectsReached.toString(),
                                    color = CreamText,
                                    fontSize = 18.sp,
                                    fontFamily = SpaceGrotesk,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        // Separator
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(1.dp)
                                .background(Gold.copy(alpha = 0.2f))
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        // ── Player Name Input ──
                        Text(
                            text = "ENTER YOUR STAGE NAME",
                            color = Gold,
                            fontSize = 9.sp,
                            fontFamily = SpaceGrotesk,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 3.sp
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(50.dp)
                                .clip(RoundedCornerShape(6.dp))
                                .background(Color(0xFF14080C).copy(alpha = 0.8f))
                                .border(
                                    1.dp,
                                    if (playerName.isNotEmpty()) Gold else Gold.copy(alpha = 0.3f),
                                    RoundedCornerShape(6.dp)
                                )
                                .padding(horizontal = 16.dp),
                            contentAlignment = Alignment.CenterStart
                        ) {
                            if (playerName.isEmpty()) {
                                Text(
                                    text = "Anonymous Juggler",
                                    color = CreamText.copy(alpha = 0.25f),
                                    fontSize = 16.sp,
                                    fontFamily = SpaceGrotesk,
                                    fontWeight = FontWeight.Medium,
                                    modifier = Modifier.fillMaxWidth(),
                                    textAlign = TextAlign.Center
                                )
                            }
                            BasicTextField(
                                value = playerName,
                                onValueChange = { newValue ->
                                    // Limit to 16 characters
                                    if (newValue.length <= 16) {
                                        playerName = newValue
                                    }
                                },
                                textStyle = TextStyle(
                                    color = Gold,
                                    fontSize = 16.sp,
                                    fontFamily = SpaceGrotesk,
                                    fontWeight = FontWeight.Bold,
                                    textAlign = TextAlign.Center,
                                    letterSpacing = 1.sp
                                ),
                                singleLine = true,
                                cursorBrush = SolidColor(Gold),
                                keyboardOptions = KeyboardOptions(
                                    capitalization = KeyboardCapitalization.Words,
                                    imeAction = ImeAction.Done
                                ),
                                keyboardActions = KeyboardActions(
                                    onDone = { focusManager.clearFocus() }
                                ),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .focusRequester(focusRequester)
                            )
                        }

                        Spacer(modifier = Modifier.height(4.dp))

                        Text(
                            text = "${playerName.length}/16",
                            color = CreamText.copy(alpha = 0.25f),
                            fontSize = 9.sp,
                            fontFamily = SpaceGrotesk
                        )
                    }
                }

                Spacer(modifier = Modifier.height(28.dp))

                // Joker monologue quote
                Typewriter(
                    text = "\"The curtain falls. The performance... adequate.\"",
                    style = TextStyle(
                        fontFamily = FontFamily.Serif,
                        fontStyle = FontStyle.Italic,
                        fontSize = 15.sp,
                        textAlign = TextAlign.Center
                    ),
                    color = Gold,
                    modifier = Modifier
                        .fillMaxWidth(0.85f)
                        .height(40.dp)
                )

                Spacer(modifier = Modifier.height(24.dp))

                // Replay Button
                PrimaryButton(
                    onClick = {
                        val name = playerName.trim().ifEmpty { "Anonymous" }
                        onSaveAndReplay(name)
                    },
                    accent = ButtonAccent.GOLD,
                    modifier = Modifier.fillMaxWidth(0.85f)
                ) {
                    Text(
                        text = "↺   PLAY AGAIN",
                        color = Color(0xFF2A0408),
                        fontSize = 18.sp,
                        fontFamily = DMSerifDisplay,
                        fontStyle = FontStyle.Italic,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.sp
                    )
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Return Home Button
                Box(
                    modifier = Modifier
                        .fillMaxWidth(0.85f)
                        .height(48.dp)
                        .clip(RoundedCornerShape(4.dp))
                        .border(1.dp, Gold.copy(alpha = 0.4f), RoundedCornerShape(4.dp))
                        .clickable(onClick = {
                            val name = playerName.trim().ifEmpty { "Anonymous" }
                            onSaveAndHome(name)
                        }),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "← BACK TO LOBBY",
                        color = Gold,
                        fontSize = 12.sp,
                        fontFamily = SpaceGrotesk,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 2.sp
                    )
                }

                Spacer(modifier = Modifier.height(32.dp))
            }
        }

        // Curtain Drapes Overlay (starts open and animates towards closed/0.12)
        CurtainDrapes(isOpen = curtainOpen)
    }
}

private fun formatTime(seconds: Int): String {
    val mins = seconds / 60
    val secs = seconds % 60
    return "%02d:%02d".format(mins, secs)
}
