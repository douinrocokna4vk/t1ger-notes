package com.Telqopu0.Kitdag.presentation.game

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateIntAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
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
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.Telqopu0.Kitdag.core.theme.SurfaceSelected
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.LiveRegionMode
import androidx.compose.ui.semantics.liveRegion
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.Telqopu0.Kitdag.R
import com.Telqopu0.Kitdag.core.config.GameConfig
import com.Telqopu0.Kitdag.core.di.AppViewModelFactory
import com.Telqopu0.Kitdag.core.theme.BorderWarm
import com.Telqopu0.Kitdag.core.theme.DeepGold
import com.Telqopu0.Kitdag.core.theme.GameOverlay
import com.Telqopu0.Kitdag.core.theme.LacquerDark
import com.Telqopu0.Kitdag.core.theme.LeverGradient
import com.Telqopu0.Kitdag.core.theme.LuckyGold
import com.Telqopu0.Kitdag.core.theme.PaleGold
import com.Telqopu0.Kitdag.core.theme.SurfaceAlt
import com.Telqopu0.Kitdag.core.theme.SurfaceCard
import com.Telqopu0.Kitdag.core.theme.TextMuted
import com.Telqopu0.Kitdag.core.theme.TextPrimary
import com.Telqopu0.Kitdag.core.theme.TextSecondary
import com.Telqopu0.Kitdag.core.theme.TigerOrange
import com.Telqopu0.Kitdag.core.ui.AppBackground
import com.Telqopu0.Kitdag.core.ui.PrimaryButton
import com.Telqopu0.Kitdag.core.ui.SymbolCell
import com.Telqopu0.Kitdag.core.ui.WoodPanel
import com.Telqopu0.Kitdag.presentation.common.ContentRes
import com.Telqopu0.Kitdag.presentation.dialog.PauseDialog
import kotlin.math.floor

@Composable
fun GameScreen(
    level: Int,
    onHome: () -> Unit,
    onFinished: (GameResult) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: GameViewModel = viewModel(factory = AppViewModelFactory)
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(level) {
        viewModel.startLevel(level)
    }

    LaunchedEffect(Unit) {
        viewModel.events.collect { event ->
            when (event) {
                is GameEvent.Finished -> onFinished(event.result)
            }
        }
    }

    DisposableEffect(Unit) {
        onDispose { viewModel.pause() }
    }

    if (state.pauseVisible) {
        PauseDialog(
            onResume = { viewModel.hidePause() },
            onMenu = onHome
        )
    }

    GameContent(
        state = state,
        onHome = onHome,
        onPause = { viewModel.showPause() },
        onSpin = { viewModel.spin() },
        onBet = { viewModel.setBet(it) },
        modifier = modifier
    )
}

@Composable
private fun GameContent(
    state: GameUiState,
    onHome: () -> Unit,
    onPause: () -> Unit,
    onSpin: () -> Unit,
    onBet: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val animatedScore by animateIntAsState(
        targetValue = state.score,
        animationSpec = tween(durationMillis = 600)
    )
    val spinningLabel = stringResource(R.string.state_spinning)
    val idleLabel = stringResource(R.string.state_ready)

    AppBackground(overlay = GameOverlay, modifier = modifier) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .safeDrawingPadding()
        ) {
            GameHeader(
                level = state.level,
                target = state.target,
                onHome = onHome,
                onPause = onPause
            )

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    AnimatedVisibility(
                        visible = state.lastGain > 0,
                        enter = fadeIn(tween(180)),
                        exit = fadeOut(tween(700))
                    ) {
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = LuckyGold,
                            shadowElevation = 6.dp
                        ) {
                            Text(
                                text = stringResource(R.string.gain_format, state.lastGain),
                                style = MaterialTheme.typography.titleMedium,
                                color = LacquerDark,
                                modifier = Modifier.padding(horizontal = 14.dp, vertical = 6.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    ReelBoard(state = state)

                    Spacer(modifier = Modifier.height(12.dp))

                    if (!state.hasSpun) {
                        Text(
                            text = stringResource(R.string.tap_spin_hint),
                            style = MaterialTheme.typography.labelMedium,
                            color = PaleGold
                        )
                    }
                }
            }

            StatStrip(
                spinsLeft = state.spinsLeft,
                score = animatedScore,
                target = state.target
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(SurfaceCard.copy(alpha = 0.94f))
                    .padding(horizontal = 20.dp, vertical = 14.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    BetChip(
                        label = stringResource(R.string.bet_x1),
                        selected = state.bet == 1,
                        enabled = !state.spinning,
                        onClick = { onBet(1) },
                        modifier = Modifier.weight(1f)
                    )
                    BetChip(
                        label = stringResource(R.string.bet_x2),
                        selected = state.bet == 2,
                        enabled = !state.spinning,
                        onClick = { onBet(2) },
                        modifier = Modifier.weight(1f)
                    )
                    BetChip(
                        label = stringResource(R.string.bet_x3),
                        selected = state.bet == 3,
                        enabled = !state.spinning,
                        onClick = { onBet(3) },
                        modifier = Modifier.weight(1f)
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                PrimaryButton(
                    text = stringResource(R.string.spin),
                    onClick = onSpin,
                    enabled = !state.spinning && !state.finished,
                    leadingIcon = R.drawable.ic_refresh,
                    gradient = LeverGradient,
                    labelColor = TextPrimary,
                    borderColor = DeepGold,
                    shadowColor = DeepGold,
                    stateLabel = if (state.spinning) spinningLabel else idleLabel
                )
            }
        }
    }
}

@Composable
private fun GameHeader(
    level: Int,
    target: Int,
    onHome: () -> Unit,
    onPause: () -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .height(64.dp),
        color = LacquerDark.copy(alpha = 0.42f)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onHome, modifier = Modifier.size(48.dp)) {
                Image(
                    painter = painterResource(R.drawable.icon_home),
                    contentDescription = stringResource(R.string.cd_home),
                    contentScale = ContentScale.Fit,
                    modifier = Modifier.size(28.dp)
                )
            }
            Column(
                modifier = Modifier.weight(1f),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = stringResource(R.string.level_format, level),
                    style = MaterialTheme.typography.titleMedium,
                    color = PaleGold,
                    textAlign = TextAlign.Center
                )
                Text(
                    text = stringResource(R.string.target_format, ContentRes.formatCount(target)),
                    style = MaterialTheme.typography.labelSmall,
                    color = BorderWarm,
                    textAlign = TextAlign.Center
                )
            }
            IconButton(onClick = onPause, modifier = Modifier.size(48.dp)) {
                Image(
                    painter = painterResource(R.drawable.icon_pause),
                    contentDescription = stringResource(R.string.cd_pause),
                    contentScale = ContentScale.Fit,
                    modifier = Modifier.size(28.dp)
                )
            }
        }
    }
}

@Composable
private fun ReelBoard(state: GameUiState) {
    BoxWithConstraints(contentAlignment = Alignment.Center) {
        val available = maxWidth - 32.dp
        val boardMax = if (available < GameConfig.BOARD_MAX_WIDTH) available else GameConfig.BOARD_MAX_WIDTH
        val frame = GameConfig.BOARD_PADDING + GameConfig.BOARD_BORDER
        val cellValue = floor(((boardMax - frame * 2) / GameConfig.COLS).value)
        val cell = cellValue.dp
        val boardSide = cell * GameConfig.COLS + frame * 2

        WoodPanel(
            modifier = Modifier
                .width(boardSide)
                .height(boardSide)
        ) {
            Column(
                modifier = Modifier
                    .align(Alignment.Center)
                    .padding(frame)
            ) {
                for (row in 0 until GameConfig.ROWS) {
                    Row {
                        for (column in 0 until GameConfig.COLS) {
                            val index = row * GameConfig.COLS + column
                            val symbol = state.grid[index]
                            SymbolCell(
                                symbol = symbol,
                                description = stringResource(ContentRes.symbolName(symbol)),
                                size = cell,
                                highlighted = state.winningCells.contains(index),
                                dimmed = !state.hasSpun
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun StatStrip(
    spinsLeft: Int,
    score: Int,
    target: Int
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp),
        shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp),
        color = SurfaceCard.copy(alpha = 0.94f),
        border = BorderStroke(1.dp, BorderWarm)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            StripItem(
                label = stringResource(R.string.label_spins),
                value = ContentRes.formatCount(spinsLeft),
                accent = TigerOrange
            )
            StripItem(
                label = stringResource(R.string.label_score),
                value = ContentRes.formatCount(score),
                accent = DeepGold,
                live = true
            )
            StripItem(
                label = stringResource(R.string.label_target),
                value = ContentRes.formatCount(target),
                accent = TextSecondary
            )
        }
    }
}

@Composable
private fun StripItem(
    label: String,
    value: String,
    accent: Color,
    live: Boolean = false
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = if (live) {
            Modifier.semantics { liveRegion = LiveRegionMode.Polite }
        } else {
            Modifier
        }
    ) {
        Text(
            text = value,
            style = MaterialTheme.typography.titleMedium,
            color = accent
        )
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = TextSecondary
        )
    }
}

@Composable
private fun BetChip(
    label: String,
    selected: Boolean,
    enabled: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val selectedLabel = stringResource(R.string.state_active)
    val plainLabel = stringResource(R.string.state_inactive)
    Surface(
        onClick = onClick,
        enabled = enabled,
        modifier = modifier
            .height(48.dp)
            .semantics { stateDescription = if (selected) selectedLabel else plainLabel },
        shape = RoundedCornerShape(12.dp),
        color = if (selected) SurfaceSelected else SurfaceAlt,
        border = BorderStroke(
            width = if (selected) 2.dp else 1.dp,
            color = if (selected) TigerOrange else BorderWarm
        )
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = label,
                style = MaterialTheme.typography.labelLarge,
                color = when {
                    !enabled -> TextMuted
                    selected -> TigerOrange
                    else -> TextSecondary
                }
            )
        }
    }
}
