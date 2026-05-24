package app.krafted.jokersjuggle.ui

import androidx.compose.animation.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import app.krafted.jokersjuggle.R
import app.krafted.jokersjuggle.ui.components.*
import app.krafted.jokersjuggle.ui.theme.*
import app.krafted.jokersjuggle.viewmodel.HomeViewModel
import app.krafted.jokersjuggle.viewmodel.JokerExpression

@Composable
fun HomeScreen(
    viewModel: HomeViewModel,
    onPlayClick: () -> Unit,
    onLeaderboardClick: () -> Unit
) {
    val bestScore by viewModel.bestScore.collectAsState()
    val maxObjects by viewModel.maxObjects.collectAsState()
    val longestTime by viewModel.longestTime.collectAsState()

    var curtainOpen by remember { mutableStateOf(0f) }
    LaunchedEffect(Unit) {
        curtainOpen = 1f
    }

    Box(modifier = Modifier.fillMaxSize()) {
        StageBackdrop {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceEvenly
            ) {
                Spacer(modifier = Modifier.height(2.dp))

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(6.dp))
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(Color(0xD93C0C14), Color(0xD914040A))
                            )
                        )
                        .border(1.dp, Gold, RoundedCornerShape(6.dp))
                        .padding(10.dp)
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        MarqueeBulbs(count = 14)
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "★  TONIGHT ONLY  ★",
                            color = Gold,
                            fontSize = 8.sp,
                            fontFamily = SpaceGrotesk,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 3.sp
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Image(
                            painter = painterResource(id = R.drawable.jok021_sym_4),
                            contentDescription = null,
                            modifier = Modifier.size(48.dp)
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = "Joker's",
                            color = CreamText,
                            fontSize = 24.sp,
                            fontFamily = DMSerifDisplay,
                            lineHeight = 24.sp
                        )
                        Text(
                            text = "Juggle",
                            color = Gold,
                            fontSize = 36.sp,
                            fontFamily = DMSerifDisplay,
                            fontStyle = androidx.compose.ui.text.font.FontStyle.Italic,
                            lineHeight = 36.sp,
                            modifier = Modifier.offset(y = (-2).dp)
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = "A · TWO-THUMB · CIRCUS",
                            color = Gold.copy(alpha = 0.8f),
                            fontSize = 8.sp,
                            fontFamily = SpaceGrotesk,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 2.sp
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        MarqueeBulbs(count = 14)
                    }
                }

                Box(
                    contentAlignment = Alignment.BottomCenter,
                    modifier = Modifier.height(100.dp)
                ) {
                    JokerPortrait(
                        expression = JokerExpression.THEATRICAL,
                        portraitSize = 80.dp
                    )
                    Box(
                        modifier = Modifier
                            .width(120.dp)
                            .height(6.dp)
                            .background(
                                Brush.radialGradient(
                                    colors = listOf(Gold.copy(alpha = 0.3f), Color.Transparent)
                                )
                            )
                    )
                }

                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        GoldFlourish(width = 50.dp)
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "BEST PERFORMANCES",
                            color = Gold.copy(alpha = 0.8f),
                            fontSize = 8.sp,
                            fontFamily = SpaceGrotesk,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 2.sp
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        GoldFlourish(width = 50.dp, flip = true)
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        PlaybillCard(
                            modifier = Modifier.weight(1f)
                        ) {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text(
                                    text = "BEST SCORE",
                                    color = Gold,
                                    fontSize = 8.sp,
                                    fontFamily = SpaceGrotesk,
                                    fontWeight = FontWeight.Bold,
                                    letterSpacing = 0.5.sp,
                                    textAlign = TextAlign.Center
                                )
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(
                                    text = bestScore.toString(),
                                    color = CreamText,
                                    fontSize = 15.sp,
                                    fontFamily = DMSerifDisplay,
                                    fontStyle = androidx.compose.ui.text.font.FontStyle.Italic,
                                    fontWeight = FontWeight.Bold,
                                    textAlign = TextAlign.Center
                                )
                            }
                        }

                        PlaybillCard(
                            modifier = Modifier.weight(1f)
                        ) {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text(
                                    text = "LONGEST RUN",
                                    color = Gold,
                                    fontSize = 8.sp,
                                    fontFamily = SpaceGrotesk,
                                    fontWeight = FontWeight.Bold,
                                    letterSpacing = 0.5.sp,
                                    textAlign = TextAlign.Center
                                )
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(
                                    text = formatTime(longestTime),
                                    color = CreamText,
                                    fontSize = 15.sp,
                                    fontFamily = DMSerifDisplay,
                                    fontStyle = androidx.compose.ui.text.font.FontStyle.Italic,
                                    fontWeight = FontWeight.Bold,
                                    textAlign = TextAlign.Center
                                )
                            }
                        }

                        PlaybillCard(
                            modifier = Modifier.weight(1f)
                        ) {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text(
                                    text = "MAX JUGGLED",
                                    color = Gold,
                                    fontSize = 8.sp,
                                    fontFamily = SpaceGrotesk,
                                    fontWeight = FontWeight.Bold,
                                    letterSpacing = 0.5.sp,
                                    textAlign = TextAlign.Center
                                )
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(
                                    text = "$maxObjects obj",
                                    color = CreamText,
                                    fontSize = 13.sp,
                                    fontFamily = DMSerifDisplay,
                                    fontStyle = androidx.compose.ui.text.font.FontStyle.Italic,
                                    fontWeight = FontWeight.Bold,
                                    textAlign = TextAlign.Center
                                )
                            }
                        }
                    }
                }

                PrimaryButton(
                    onClick = onPlayClick,
                    accent = ButtonAccent.GOLD,
                    modifier = Modifier.fillMaxWidth(0.9f)
                ) {
                    Text(
                        text = "▶   TAKE THE STAGE",
                        color = Color(0xFF2A0408),
                        fontSize = 18.sp,
                        fontFamily = DMSerifDisplay,
                        fontStyle = androidx.compose.ui.text.font.FontStyle.Italic,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.sp
                    )
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth(0.9f),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(44.dp)
                            .clip(RoundedCornerShape(4.dp))
                            .background(Color.Transparent)
                            .border(1.dp, Gold.copy(alpha = 0.4f), RoundedCornerShape(4.dp))
                            .clickable(onClick = onLeaderboardClick),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "♠  LEADERBOARD",
                            color = Gold,
                            fontSize = 11.sp,
                            fontFamily = SpaceGrotesk,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 2.sp
                        )
                    }
                }

                Spacer(modifier = Modifier.height(2.dp))
            }
        }

        CurtainDrapes(isOpen = curtainOpen)
    }
}

private fun formatTime(seconds: Int): String {
    val mins = seconds / 60
    val secs = seconds % 60
    return "%02d:%02d".format(mins, secs)
}
