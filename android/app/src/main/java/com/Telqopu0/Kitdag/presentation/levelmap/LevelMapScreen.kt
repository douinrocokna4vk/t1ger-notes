package com.Telqopu0.Kitdag.presentation.levelmap

import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.Telqopu0.Kitdag.R
import com.Telqopu0.Kitdag.core.di.AppViewModelFactory
import com.Telqopu0.Kitdag.core.theme.BorderWarm
import com.Telqopu0.Kitdag.core.theme.LevelMapOverlay
import com.Telqopu0.Kitdag.core.theme.PaperGradient
import com.Telqopu0.Kitdag.core.theme.SurfaceMuted
import com.Telqopu0.Kitdag.core.theme.TextMuted
import com.Telqopu0.Kitdag.core.theme.TextPrimary
import com.Telqopu0.Kitdag.core.theme.TigerOrange
import com.Telqopu0.Kitdag.core.ui.CoinBadge
import com.Telqopu0.Kitdag.core.ui.ScreenScaffold
import com.Telqopu0.Kitdag.core.ui.StarRow
import com.Telqopu0.Kitdag.domain.model.LevelProgress
import com.Telqopu0.Kitdag.presentation.common.ContentRes

@Composable
fun LevelMapScreen(
    onBack: () -> Unit,
    onLevel: (Int) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: LevelMapViewModel = viewModel(factory = AppViewModelFactory)
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.refresh()
    }

    ScreenScaffold(
        title = stringResource(R.string.level_map_title),
        backDescription = stringResource(R.string.cd_back),
        onBack = onBack,
        overlay = LevelMapOverlay,
        modifier = modifier,
        trailing = { CoinBadge(value = ContentRes.formatCount(state.coins)) }
    ) {
        LazyVerticalGrid(
            columns = GridCells.Fixed(3),
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            contentPadding = PaddingValues(20.dp),
            horizontalArrangement = Arrangement.spacedBy(14.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            items(items = state.levels, key = { it.level }) { level ->
                LevelMedallion(
                    level = level,
                    current = level.level == state.currentLevel,
                    onClick = { onLevel(level.level) }
                )
            }
        }
    }
}

@Composable
private fun LevelMedallion(
    level: LevelProgress,
    current: Boolean,
    onClick: () -> Unit
) {
    val transition = rememberInfiniteTransition()
    val pulse by transition.animateFloat(
        initialValue = 0.35f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(tween(1400), RepeatMode.Reverse)
    )
    val lockedLabel = stringResource(R.string.state_locked)
    val unlockedLabel = stringResource(R.string.state_unlocked)
    val description = stringResource(R.string.cd_level_medallion, level.level, level.stars)

    Surface(
        onClick = onClick,
        enabled = level.unlocked,
        modifier = Modifier
            .size(96.dp)
            .alpha(if (level.unlocked) 1f else 0.6f)
            .semantics {
                contentDescription = description
                stateDescription = if (level.unlocked) unlockedLabel else lockedLabel
            },
        shape = RoundedCornerShape(18.dp),
        color = if (level.unlocked) Color.Transparent else SurfaceMuted,
        border = BorderStroke(
            width = 2.dp,
            color = when {
                current && level.unlocked -> TigerOrange.copy(alpha = pulse)
                level.unlocked -> BorderWarm
                else -> SurfaceMuted
            }
        ),
        shadowElevation = if (level.unlocked) 6.dp else 0.dp
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    if (level.unlocked) {
                        Brush.verticalGradient(PaperGradient)
                    } else {
                        Brush.verticalGradient(listOf(SurfaceMuted, SurfaceMuted))
                    }
                ),
            contentAlignment = Alignment.Center
        ) {
            if (level.unlocked) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = ContentRes.formatCount(level.level),
                        style = MaterialTheme.typography.headlineSmall,
                        color = TextPrimary
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    StarRow(earned = level.stars, starSize = 14.dp, gap = 3.dp)
                }
            } else {
                Icon(
                    painter = painterResource(R.drawable.ic_lock),
                    contentDescription = null,
                    tint = TextMuted,
                    modifier = Modifier.size(26.dp)
                )
            }
        }
    }
}
