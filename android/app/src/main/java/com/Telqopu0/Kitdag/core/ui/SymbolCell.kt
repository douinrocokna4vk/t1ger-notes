package com.Telqopu0.Kitdag.core.ui

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.Telqopu0.Kitdag.core.theme.LacquerFelt
import com.Telqopu0.Kitdag.core.theme.LuckyGold
import com.Telqopu0.Kitdag.domain.model.Symbol

@Composable
fun SymbolCell(
    symbol: Symbol,
    description: String,
    size: Dp,
    modifier: Modifier = Modifier,
    highlighted: Boolean = false,
    dimmed: Boolean = false
) {
    val scale by animateFloatAsState(targetValue = if (highlighted) 1.12f else 1f)
    val shape = RoundedCornerShape(12.dp)
    Box(
        modifier = modifier
            .size(size)
            .padding(5.dp)
            .background(color = LacquerFelt.copy(alpha = 0.55f), shape = shape)
            .border(
                border = BorderStroke(
                    width = if (highlighted) 2.dp else 1.dp,
                    color = if (highlighted) LuckyGold else LuckyGold.copy(alpha = 0.22f)
                ),
                shape = shape
            ),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(drawableForSymbol(symbol)),
            contentDescription = description,
            contentScale = ContentScale.Fit,
            modifier = Modifier
                .size(size * 0.62f)
                .alpha(if (dimmed) 0.55f else 1f)
                .graphicsLayer {
                    scaleX = scale
                    scaleY = scale
                }
        )
    }
}
