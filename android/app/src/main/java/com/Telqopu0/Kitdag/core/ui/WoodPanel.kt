package com.Telqopu0.Kitdag.core.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.Telqopu0.Kitdag.core.theme.GoldSweep
import com.Telqopu0.Kitdag.core.theme.WoodGradient

@Composable
fun WoodPanel(
    modifier: Modifier = Modifier,
    shape: Shape = RoundedCornerShape(20.dp),
    borderWidth: Dp = 3.dp,
    elevation: Dp = 16.dp,
    gradient: List<Color> = WoodGradient,
    content: @Composable BoxScope.() -> Unit
) {
    Surface(
        modifier = modifier,
        shape = shape,
        color = Color.Transparent,
        border = BorderStroke(borderWidth, Brush.sweepGradient(GoldSweep)),
        shadowElevation = elevation
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Brush.verticalGradient(gradient)),
            content = content
        )
    }
}
