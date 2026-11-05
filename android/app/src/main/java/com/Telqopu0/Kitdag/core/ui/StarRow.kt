package com.Telqopu0.Kitdag.core.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.Telqopu0.Kitdag.R
import com.Telqopu0.Kitdag.core.theme.LuckyGold
import com.Telqopu0.Kitdag.core.theme.SurfaceMuted

@Composable
fun StarRow(
    earned: Int,
    modifier: Modifier = Modifier,
    total: Int = 3,
    starSize: Dp = 16.dp,
    gap: Dp = 4.dp
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(gap),
        verticalAlignment = Alignment.CenterVertically
    ) {
        repeat(total) { index ->
            Icon(
                painter = painterResource(R.drawable.ic_star),
                contentDescription = null,
                tint = if (index < earned) LuckyGold else SurfaceMuted,
                modifier = Modifier.size(starSize)
            )
        }
    }
}
