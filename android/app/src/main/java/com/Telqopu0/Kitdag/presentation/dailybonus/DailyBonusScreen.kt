package com.Telqopu0.Kitdag.presentation.dailybonus

import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.Telqopu0.Kitdag.R
import com.Telqopu0.Kitdag.core.di.AppViewModelFactory
import com.Telqopu0.Kitdag.core.theme.BonusOverlay
import com.Telqopu0.Kitdag.core.theme.BorderWarm
import com.Telqopu0.Kitdag.core.theme.SurfaceAlt
import com.Telqopu0.Kitdag.core.theme.SurfaceMuted
import com.Telqopu0.Kitdag.core.theme.SurfaceSelected
import com.Telqopu0.Kitdag.core.theme.Success
import com.Telqopu0.Kitdag.core.theme.TextPrimary
import com.Telqopu0.Kitdag.core.theme.TextSecondary
import com.Telqopu0.Kitdag.core.theme.TigerOrange
import com.Telqopu0.Kitdag.core.ui.CoinBadge
import com.Telqopu0.Kitdag.core.ui.PrimaryButton
import com.Telqopu0.Kitdag.core.ui.ScreenScaffold
import com.Telqopu0.Kitdag.presentation.common.ContentRes

@Composable
fun DailyBonusScreen(
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: DailyBonusViewModel = viewModel(factory = AppViewModelFactory)
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.refresh()
    }

    val transition = rememberInfiniteTransition()
    val glow by transition.animateFloat(
        initialValue = 0.4f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(tween(1200), RepeatMode.Reverse)
    )
    val float by transition.animateFloat(
        initialValue = -6f,
        targetValue = 6f,
        animationSpec = infiniteRepeatable(tween(3000), RepeatMode.Reverse)
    )

    ScreenScaffold(
        title = stringResource(R.string.daily_bonus_title),
        backDescription = stringResource(R.string.cd_back),
        onBack = onBack,
        overlay = BonusOverlay,
        modifier = modifier,
        trailing = { CoinBadge(value = ContentRes.formatCount(state.coins)) }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .padding(horizontal = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Image(
                painter = painterResource(R.drawable.sprite_2),
                contentDescription = stringResource(R.string.symbol_ingot),
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .size(120.dp)
                    .graphicsLayer { translationY = float }
            )

            Spacer(modifier = Modifier.height(20.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                for (index in 0 until 4) {
                    DayTile(
                        index = index,
                        amount = state.amounts.getOrElse(index) { 0 },
                        claimed = index < state.claimedDays,
                        ready = index == state.claimedDays && state.ready,
                        glow = glow,
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                for (index in 4 until 7) {
                    DayTile(
                        index = index,
                        amount = state.amounts.getOrElse(index) { 0 },
                        claimed = index < state.claimedDays,
                        ready = index == state.claimedDays && state.ready,
                        glow = glow,
                        modifier = Modifier.weight(1f)
                    )
                }
                Spacer(modifier = Modifier.weight(1f))
            }

            Spacer(modifier = Modifier.height(24.dp))

            PrimaryButton(
                text = if (state.ready) {
                    stringResource(R.string.claim)
                } else {
                    stringResource(R.string.come_back_tomorrow)
                },
                onClick = { viewModel.claim() },
                enabled = state.ready
            )

            if (!state.ready) {
                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    text = ContentRes.formatDuration(state.remainingMillis),
                    style = MaterialTheme.typography.labelMedium,
                    color = TextSecondary
                )
            }
        }
    }
}

@Composable
private fun DayTile(
    index: Int,
    amount: Int,
    claimed: Boolean,
    ready: Boolean,
    glow: Float,
    modifier: Modifier = Modifier
) {
    val claimedLabel = stringResource(R.string.state_claimed)
    val readyLabel = stringResource(R.string.state_ready)
    val lockedLabel = stringResource(R.string.state_locked)
    Surface(
        modifier = modifier
            .height(76.dp)
            .semantics {
                stateDescription = when {
                    claimed -> claimedLabel
                    ready -> readyLabel
                    else -> lockedLabel
                }
            },
        shape = RoundedCornerShape(14.dp),
        color = when {
            claimed -> SurfaceAlt
            ready -> SurfaceSelected
            else -> SurfaceMuted.copy(alpha = 0.7f)
        },
        border = BorderStroke(
            width = if (ready) 2.dp else 1.dp,
            color = if (ready) TigerOrange.copy(alpha = glow) else BorderWarm
        ),
        shadowElevation = if (ready) 6.dp else 0.dp
    ) {
        Box(contentAlignment = Alignment.Center) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = stringResource(R.string.day_format, index + 1),
                    style = MaterialTheme.typography.labelSmall,
                    color = TextSecondary,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(4.dp))
                if (claimed) {
                    Icon(
                        painter = painterResource(R.drawable.ic_check),
                        contentDescription = null,
                        tint = Success,
                        modifier = Modifier.size(20.dp)
                    )
                } else {
                    Text(
                        text = ContentRes.formatCount(amount),
                        style = MaterialTheme.typography.titleMedium,
                        color = TextPrimary,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}
