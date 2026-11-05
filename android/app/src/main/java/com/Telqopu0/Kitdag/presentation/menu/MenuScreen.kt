package com.Telqopu0.Kitdag.presentation.menu

import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.Telqopu0.Kitdag.R
import com.Telqopu0.Kitdag.core.di.AppViewModelFactory
import com.Telqopu0.Kitdag.core.theme.BorderWarm
import com.Telqopu0.Kitdag.core.theme.LuckyGold
import com.Telqopu0.Kitdag.core.theme.MenuOverlay
import com.Telqopu0.Kitdag.core.theme.PaperGradient
import com.Telqopu0.Kitdag.core.theme.TextPrimary
import com.Telqopu0.Kitdag.core.theme.TextSecondary
import com.Telqopu0.Kitdag.core.theme.TigerOrange
import com.Telqopu0.Kitdag.core.ui.AppBackground
import com.Telqopu0.Kitdag.core.ui.CoinBadge
import com.Telqopu0.Kitdag.core.ui.PrimaryButton
import com.Telqopu0.Kitdag.core.ui.SecondaryButton
import com.Telqopu0.Kitdag.core.ui.StatCard
import com.Telqopu0.Kitdag.presentation.common.ContentRes

@Composable
fun MenuScreen(
    onPlay: (Int) -> Unit,
    onLevels: () -> Unit,
    onBonus: () -> Unit,
    onRewards: () -> Unit,
    onAwards: () -> Unit,
    onStats: () -> Unit,
    onSettings: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: MenuViewModel = viewModel(factory = AppViewModelFactory)
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.refresh()
    }

    MenuContent(
        state = state,
        onPlay = { onPlay(state.level) },
        onLevels = onLevels,
        onBonus = onBonus,
        onRewards = onRewards,
        onAwards = onAwards,
        onStats = onStats,
        onSettings = onSettings,
        modifier = modifier
    )
}

@Composable
private fun MenuContent(
    state: MenuUiState,
    onPlay: () -> Unit,
    onLevels: () -> Unit,
    onBonus: () -> Unit,
    onRewards: () -> Unit,
    onAwards: () -> Unit,
    onStats: () -> Unit,
    onSettings: () -> Unit,
    modifier: Modifier = Modifier
) {
    val transition = rememberInfiniteTransition()
    val breathe by transition.animateFloat(
        initialValue = 1f,
        targetValue = 1.035f,
        animationSpec = infiniteRepeatable(tween(4000), RepeatMode.Reverse)
    )

    AppBackground(overlay = MenuOverlay, modifier = modifier) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .safeDrawingPadding()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .padding(horizontal = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = onSettings,
                    modifier = Modifier.size(48.dp)
                ) {
                    Icon(
                        painter = painterResource(R.drawable.ic_settings),
                        contentDescription = stringResource(R.string.cd_settings),
                        tint = TextPrimary,
                        modifier = Modifier.size(26.dp)
                    )
                }
                Spacer(modifier = Modifier.weight(1f))
                CoinBadge(value = ContentRes.formatCount(state.coins))
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .clipToBounds(),
                contentAlignment = Alignment.Center
            ) {
                Canvas(modifier = Modifier.fillMaxSize()) {
                    drawCircle(
                        brush = Brush.radialGradient(
                            colors = listOf(LuckyGold.copy(alpha = 0.22f), Color.Transparent),
                            center = Offset(size.width / 2f, size.height * 0.58f),
                            radius = size.minDimension * 0.62f
                        ),
                        radius = size.minDimension * 0.62f,
                        center = Offset(size.width / 2f, size.height * 0.58f)
                    )
                }
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Image(
                        painter = painterResource(R.drawable.logo),
                        contentDescription = stringResource(R.string.cd_logo),
                        contentScale = ContentScale.Fit,
                        modifier = Modifier.size(96.dp)
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Image(
                        painter = painterResource(R.drawable.sprite_1),
                        contentDescription = stringResource(R.string.symbol_tiger),
                        contentScale = ContentScale.Fit,
                        modifier = Modifier
                            .size(168.dp)
                            .graphicsLayer {
                                scaleX = breathe
                                scaleY = breathe
                            }
                    )
                }
            }

            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(topStart = 30.dp, topEnd = 30.dp),
                color = Color.Transparent,
                shadowElevation = 20.dp
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Brush.verticalGradient(PaperGradient))
                        .padding(horizontal = 20.dp, vertical = 14.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(
                        modifier = Modifier
                            .width(44.dp)
                            .height(4.dp)
                            .background(color = BorderWarm, shape = RoundedCornerShape(2.dp))
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = stringResource(R.string.brand_title),
                        style = MaterialTheme.typography.headlineMedium,
                        color = TextPrimary,
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = stringResource(R.string.menu_tagline),
                        style = MaterialTheme.typography.bodyMedium,
                        color = TextSecondary,
                        textAlign = TextAlign.Center
                    )

                    if (state.bestScore > 0) {
                        Spacer(modifier = Modifier.height(12.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            StatCard(
                                label = stringResource(R.string.label_level),
                                value = ContentRes.formatCount(state.level),
                                accent = TigerOrange,
                                modifier = Modifier.weight(1f)
                            )
                            StatCard(
                                label = stringResource(R.string.label_best),
                                value = ContentRes.formatCount(state.bestScore),
                                accent = LuckyGold,
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    PrimaryButton(
                        text = stringResource(R.string.play),
                        onClick = onPlay,
                        leadingIcon = R.drawable.ic_play_arrow
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        SecondaryButton(
                            text = stringResource(R.string.levels),
                            onClick = onLevels,
                            modifier = Modifier.weight(1f)
                        )
                        SecondaryButton(
                            text = stringResource(R.string.bonus),
                            onClick = onBonus,
                            accent = if (state.bonusReady) TigerOrange else BorderWarm,
                            badge = state.bonusReady,
                            modifier = Modifier.weight(1f)
                        )
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        MenuLink(text = stringResource(R.string.rewards), onClick = onRewards)
                        MenuLink(text = stringResource(R.string.awards), onClick = onAwards)
                        MenuLink(text = stringResource(R.string.stats), onClick = onStats)
                    }
                }
            }
        }
    }
}

@Composable
private fun MenuLink(
    text: String,
    onClick: () -> Unit
) {
    TextButton(
        onClick = onClick,
        modifier = Modifier.height(48.dp)
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.labelLarge,
            color = TextSecondary
        )
    }
}
