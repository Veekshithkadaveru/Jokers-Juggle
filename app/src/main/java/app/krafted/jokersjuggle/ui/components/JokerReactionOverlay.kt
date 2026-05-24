package app.krafted.jokersjuggle.ui.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import app.krafted.jokersjuggle.ui.theme.CreamText
import app.krafted.jokersjuggle.ui.theme.DeepStage
import app.krafted.jokersjuggle.ui.theme.Gold
import app.krafted.jokersjuggle.ui.theme.MarqueeDim
import app.krafted.jokersjuggle.ui.theme.SpaceGrotesk
import app.krafted.jokersjuggle.viewmodel.GameUiState
import kotlinx.coroutines.delay

@Composable
fun JokerReactionOverlay(state: GameUiState, modifier: Modifier = Modifier) {
    Box(modifier.fillMaxSize()) {
        val pulse = remember { Animatable(1f) }
        
        LaunchedEffect(state.jokerExpression) {
            pulse.snapTo(1f)
            pulse.animateTo(1.18f, spring())
            pulse.animateTo(1f, spring())
        }

        JokerPortrait(
            expression = state.jokerExpression,
            portraitSize = 90.dp,
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(top = 56.dp, end = 8.dp)
                .scale(pulse.value)
        )

        if (state.jokerQuote.isNotEmpty()) {
            QuoteBar(
                quote = state.jokerQuote,
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .fillMaxWidth(0.74f)
                    .padding(start = 10.dp, top = 150.dp)
            )
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
        modifier = modifier
            .background(
                Brush.verticalGradient(
                    colors = listOf(DeepStage.copy(alpha = 0.92f), Color(0xEB0A0408))
                ),
                shape = shape
            )
            .border(1.dp, MarqueeDim, shape),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .width(3.dp)
                .height(34.dp)
                .background(Gold)
        )
        Text(
            text = "“",
            color = Gold,
            fontSize = 16.sp,
            modifier = Modifier.padding(start = 8.dp)
        )
        Spacer(Modifier.width(6.dp))
        val cursor = if (shown.length < quote.length) "▌" else ""
        Text(
            text = shown + cursor,
            color = CreamText,
            fontSize = 12.sp,
            fontStyle = FontStyle.Italic,
            fontFamily = FontFamily.Serif,
            textAlign = TextAlign.Start,
            modifier = Modifier.padding(end = 10.dp, top = 6.dp, bottom = 6.dp)
        )
    }
}
