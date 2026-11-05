package com.Telqopu0.Kitdag.presentation.gameover

import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
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
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.LiveRegionMode
import androidx.compose.ui.semantics.liveRegion
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.Telqopu0.Kitdag.R
import com.Telqopu0.Kitdag.core.di.AppViewModelFactory
import com.Telqopu0.Kitdag.core.theme.BorderWarm
import com.Telqopu0.Kitdag.core.theme.DeepGold
import com.Telqopu0.Kitdag.core.theme.LoseOverlay
import com.Telqopu0.Kitdag.core.theme.LuckyGold
import com.Telqopu0.Kitdag.core.theme.Success
import com.Telqopu0.Kitdag.core.theme.SurfaceCard
import com.Telqopu0.Kitdag.core.theme.TextSecondary
import com.Telqopu0.Kitdag.core.theme.TigerOrange
import com.Telqopu0.Kitdag.core.theme.WinOverlay
import com.Telqopu0.Kitdag.core.ui.AppBackground
import com.Telqopu0.Kitdag.core.ui.PrimaryButton
import com.Telqopu0.Kitdag.core.ui.SecondaryButton
import com.Telqopu0.Kitdag.core.ui.StarRow
import com.Telqopu0.Kitdag.core.ui.StatCard
import com.Telqopu0.Kitdag.presentation.common.ContentRes

@Composable
fun GameOverScreen(
    level: Int,
    score: Int,
    target: Int,
    stars: Int,
    coins: Int,
    newBest: Boolean,
    onPlayAgain: (Int) -> Unit,
    onNextLevel: (Int) -> Unit,
    onMenu: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: GameOverViewModel = viewModel(factory = AppViewModelFactory)
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(level, score) {
        viewModel.load(level)
    }

    val won = stars > 0
    val transition = rememberInfiniteTransition()
    val breathe by transition.animateFloat(
        initialValue = 1f,
        targetValue = 1.06f,
        animationSpec = infiniteRepeatable(tween(2000), RepeatMode.Reverse)
    )

    AppBackground(
        overlay = if (won) WinOverlay else LoseOverlay,
        modifier = modifier
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .safeDrawingPadding()
                .padding(horizontal = 24.dp, vertical = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Surface(
                modifier = Modifier.widthIn(max = 340.dp),
                shape = RoundedCornerShape(24.dp),
                color = SurfaceCard.copy(alpha = 0.97f),
                border = BorderStroke(2.dp, BorderWarm),
                shadowElevation = 18.dp
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Image(
                        painter = painterResource(
                            if (won) R.drawable.sprite_1 else R.drawable.sprite_4
                        ),
                        contentDescription = stringResource(
                            if (won) R.string.symbol_tiger else R.string.symbol_lantern
                        ),
                        contentScale = ContentScale.Fit,
                        modifier = Modifier
                            .size(92.dp)
                            .graphicsLayer {
                                scaleX = breathe
                                scaleY = breathe
                            }
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Box(
                        modifier = Modifier
                            .width(150.dp)
                            .height(2.dp)
                            .clip(RoundedCornerShape(1.dp))
                            .background(LuckyGold.copy(alpha = 0.6f))
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = stringResource(
                            if (won) R.string.result_win else R.string.result_lose
                        ),
                        style = MaterialTheme.typography.headlineMedium,
                        color = if (won) TigerOrange else TextSecondary,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.semantics { liveRegion = LiveRegionMode.Polite }
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Box(
                        modifier = Modifier
                            .width(150.dp)
                            .height(2.dp)
                            .clip(RoundedCornerShape(1.dp))
                            .background(LuckyGold.copy(alpha = 0.6f))
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    StarRow(earned = stars, starSize = 34.dp, gap = 10.dp)

                    Spacer(modifier = Modifier.height(18.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        StatCard(
                            label = stringResource(R.string.label_score),
                            value = ContentRes.formatCount(score),
                            accent = TigerOrange,
                            modifier = Modifier.weight(1f)
                        )
                        StatCard(
                            label = stringResource(R.string.label_coins),
                            value = ContentRes.formatCount(coins),
                            accent = DeepGold,
                            modifier = Modifier.weight(1f)
                        )
                        if (state.bestScore > 0) {
                            StatCard(
                                label = stringResource(R.string.label_best),
                                value = ContentRes.formatCount(state.bestScore),
                                accent = Success,
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }

                    if (newBest) {
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = stringResource(R.string.new_best),
                            style = MaterialTheme.typography.labelLarge,
                            color = Success
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = stringResource(
                            R.string.target_format,
                            ContentRes.formatCount(target)
                        ),
                        style = MaterialTheme.typography.labelSmall,
                        color = TextSecondary
                    )
                }
            }

            Spacer(modifier = Modifier.height(18.dp))

            Column(
                modifier = Modifier.widthIn(max = 340.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                PrimaryButton(
                    text = stringResource(R.string.play_again),
                    onClick = { onPlayAgain(level) },
                    leadingIcon = R.drawable.ic_play_arrow
                )
                if (state.nextLevel > 0) {
                    SecondaryButton(
                        text = stringResource(R.string.next_level),
                        onClick = { onNextLevel(state.nextLevel) },
                        accent = TigerOrange
                    )
                }
                SecondaryButton(
                    text = stringResource(R.string.menu),
                    onClick = onMenu
                )
            }
        }
    }
}
