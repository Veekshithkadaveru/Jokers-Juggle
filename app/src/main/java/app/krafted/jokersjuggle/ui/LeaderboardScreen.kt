package app.krafted.jokersjuggle.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import app.krafted.jokersjuggle.data.db.ScoreRecord
import app.krafted.jokersjuggle.ui.components.*
import app.krafted.jokersjuggle.ui.theme.*
import app.krafted.jokersjuggle.viewmodel.LeaderboardViewModel

@Composable
fun LeaderboardScreen(
    viewModel: LeaderboardViewModel,
    onBackClick: () -> Unit
) {
    val scores by viewModel.scores.collectAsState()

    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        StageBackdrop {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp, vertical = 24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(32.dp))

                // Back navigation text button
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Start
                ) {
                    Text(
                        text = "← BACK",
                        color = Gold,
                        fontSize = 11.sp,
                        fontFamily = SpaceGrotesk,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 2.sp,
                        modifier = Modifier
                            .clickable(onClick = onBackClick)
                            .padding(8.dp)
                    )
                }

                // Header title
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    GoldFlourish(width = 60.dp)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "HALL OF",
                        color = Gold,
                        fontSize = 10.sp,
                        fontFamily = SpaceGrotesk,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 5.sp
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    GoldFlourish(width = 60.dp, flip = true)
                }

                Text(
                    text = "Performers",
                    color = Gold,
                    fontSize = 40.sp,
                    fontFamily = DMSerifDisplay,
                    fontStyle = FontStyle.Italic,
                    textAlign = TextAlign.Center,
                    lineHeight = 40.sp,
                    modifier = Modifier.offset(y = (-2).dp)
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Scores list container
                if (scores.isEmpty()) {
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "No records yet. Go make history!",
                            color = CreamText.copy(alpha = 0.4f),
                            fontSize = 14.sp,
                            fontFamily = SpaceGrotesk,
                            fontWeight = FontWeight.Medium
                        )
                    }
                } else {
                    LazyColumn(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(12.dp))
                            .background(Color.Black.copy(alpha = 0.3f))
                            .border(1.dp, MarqueeDim, RoundedCornerShape(12.dp))
                            .padding(6.dp),
                        verticalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        itemsIndexed(scores) { index, record ->
                            LeaderboardItem(rank = index + 1, record = record)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Bottom Back button
                PrimaryButton(
                    onClick = onBackClick,
                    accent = ButtonAccent.GOLD,
                    modifier = Modifier
                        .fillMaxWidth(0.7f)
                        .padding(bottom = 8.dp)
                ) {
                    Text(
                        text = "BACK TO LOBBY",
                        color = Color(0xFF2A0408),
                        fontSize = 14.sp,
                        fontFamily = SpaceGrotesk,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.sp
                    )
                }
            }
        }
    }
}

@Composable
private fun LeaderboardItem(rank: Int, record: ScoreRecord) {
    val medalColor = when (rank) {
        1 -> Gold
        2 -> Color(0xFFD8D8D8) // Silver
        3 -> Color(0xFFCD7F32) // Bronze
        else -> Color.Transparent
    }

    val medalLabel = when (rank) {
        1 -> "★ HEADLINER"
        2 -> "UNDERSTUDY"
        3 -> "RISING STAR"
        else -> null
    }

    val rankSuffixText = when (rank) {
        1 -> "1st"
        2 -> "2nd"
        3 -> "3rd"
        else -> "${rank}th"
    }

    // Row Background (Gradient for Top 3, transparent/plain for others)
    val itemBgBrush = when (rank) {
        1 -> Brush.horizontalGradient(
            colors = listOf(Gold.copy(alpha = 0.25f), Gold.copy(alpha = 0.05f))
        )
        2 -> Brush.horizontalGradient(
            colors = listOf(Color(0xFF3C3018).copy(alpha = 0.6f), Color(0xFF14080C).copy(alpha = 0.6f))
        )
        3 -> Brush.horizontalGradient(
            colors = listOf(Color(0xFF3C3018).copy(alpha = 0.6f), Color(0xFF14080C).copy(alpha = 0.6f))
        )
        else -> null
    }

    val borderStrokeColor = when (rank) {
        1 -> Gold
        2, 3 -> Color(0xFF5A3A08)
        else -> Color(0xFF3A2A18)
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(4.dp))
            .drawBehind {
                if (itemBgBrush != null) {
                    drawRoundRect(brush = itemBgBrush)
                } else {
                    drawRoundRect(color = Color(0xFF14080C).copy(alpha = 0.6f))
                }
            }
            .border(1.dp, borderStrokeColor, RoundedCornerShape(4.dp))
            .padding(horizontal = 14.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Rank Badge
        Box(
            modifier = Modifier
                .size(28.dp)
                .clip(CircleShape)
                .background(
                    if (medalColor != Color.Transparent) {
                        Brush.radialGradient(
                            colors = listOf(medalColor, Color(0xFF2A1804))
                        )
                    } else {
                        Brush.linearGradient(
                            colors = listOf(Color.Transparent, Color.Transparent)
                        )
                    }
                )
                .border(
                    1.dp,
                    if (medalColor != Color.Transparent) medalColor else Color(0xFF3A2A18),
                    CircleShape
                ),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = rank.toString(),
                color = if (medalColor != Color.Transparent) Color(0xFF2A0408) else CreamText,
                fontSize = 14.sp,
                fontFamily = DMSerifDisplay,
                fontStyle = FontStyle.Italic,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(modifier = Modifier.width(14.dp))

        // Performance Stats details
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = "Score: ${record.score}",
                color = if (rank == 1) Color(0xFFFFD860) else CreamText,
                fontSize = 15.sp,
                fontFamily = SpaceGrotesk,
                fontWeight = FontWeight.Bold,
                letterSpacing = 0.5.sp
            )
            Spacer(modifier = Modifier.height(2.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "⏱ ${formatTime(record.timeSurvivedSeconds)}",
                    color = CreamText.copy(alpha = 0.5f),
                    fontSize = 11.sp,
                    fontFamily = SpaceGrotesk
                )
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    text = "🃏 ${record.maxObjectsReached} obj",
                    color = CreamText.copy(alpha = 0.5f),
                    fontSize = 11.sp,
                    fontFamily = SpaceGrotesk
                )
            }
            if (medalLabel != null) {
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = medalLabel,
                    color = medalColor,
                    fontSize = 8.sp,
                    fontFamily = SpaceGrotesk,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.sp
                )
            }
        }

        // Rank designation
        Text(
            text = rankSuffixText,
            color = if (medalColor != Color.Transparent) medalColor else CreamText.copy(alpha = 0.4f),
            fontSize = 13.sp,
            fontFamily = SpaceGrotesk,
            fontWeight = FontWeight.Bold
        )
    }
}

private fun formatTime(seconds: Int): String {
    val mins = seconds / 60
    val secs = seconds % 60
    return "%02d:%02d".format(mins, secs)
}
