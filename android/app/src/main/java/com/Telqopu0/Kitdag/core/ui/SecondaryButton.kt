package com.Telqopu0.Kitdag.core.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.Telqopu0.Kitdag.core.theme.BorderWarm
import com.Telqopu0.Kitdag.core.theme.SurfaceCard
import com.Telqopu0.Kitdag.core.theme.SurfaceMuted
import com.Telqopu0.Kitdag.core.theme.TextMuted
import com.Telqopu0.Kitdag.core.theme.TextPrimary
import com.Telqopu0.Kitdag.core.theme.TigerOrange

@Composable
fun SecondaryButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    accent: Color = BorderWarm,
    labelColor: Color = TextPrimary,
    height: Dp = 50.dp,
    badge: Boolean = false,
    stateLabel: String? = null
) {
    val description = stateLabel
    Box(modifier = modifier) {
        OutlinedButton(
            onClick = onClick,
            enabled = enabled,
            shape = RoundedCornerShape(14.dp),
            border = BorderStroke(1.5.dp, if (enabled) accent else BorderWarm),
            colors = ButtonDefaults.outlinedButtonColors(
                containerColor = SurfaceCard,
                contentColor = labelColor,
                disabledContainerColor = SurfaceMuted,
                disabledContentColor = TextMuted
            ),
            modifier = Modifier
                .fillMaxWidth()
                .height(height)
                .then(
                    if (description != null) {
                        Modifier.semantics { stateDescription = description }
                    } else {
                        Modifier
                    }
                )
        ) {
            Text(
                text = text,
                style = MaterialTheme.typography.labelLarge,
                color = if (enabled) labelColor else TextMuted
            )
        }
        if (badge) {
            Box(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .size(10.dp)
                    .background(
                        color = TigerOrange,
                        shape = RoundedCornerShape(5.dp)
                    )
            )
        }
    }
}
