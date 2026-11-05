package com.Telqopu0.Kitdag.presentation.rewards

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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
import com.Telqopu0.Kitdag.core.theme.BorderWarm
import com.Telqopu0.Kitdag.core.theme.DeepGold
import com.Telqopu0.Kitdag.core.theme.RewardsOverlay
import com.Telqopu0.Kitdag.core.theme.Success
import com.Telqopu0.Kitdag.core.theme.SurfaceCard
import com.Telqopu0.Kitdag.core.theme.TextPrimary
import com.Telqopu0.Kitdag.core.theme.TextSecondary
import com.Telqopu0.Kitdag.core.theme.TigerOrange
import com.Telqopu0.Kitdag.core.ui.CoinBadge
import com.Telqopu0.Kitdag.core.ui.ScreenScaffold
import com.Telqopu0.Kitdag.core.ui.SecondaryButton
import com.Telqopu0.Kitdag.core.ui.drawableForKey
import com.Telqopu0.Kitdag.presentation.common.ContentRes

@Composable
fun RewardsScreen(
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: RewardsViewModel = viewModel(factory = AppViewModelFactory)
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.refresh()
    }

    ScreenScaffold(
        title = stringResource(R.string.rewards_title),
        backDescription = stringResource(R.string.cd_back),
        onBack = onBack,
        overlay = RewardsOverlay,
        modifier = modifier,
        trailing = { CoinBadge(value = ContentRes.formatCount(state.coins)) }
    ) {
        if (!state.anyAffordable && state.rows.none { it.owned }) {
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 12.dp),
                shape = RoundedCornerShape(16.dp),
                color = SurfaceCard,
                border = BorderStroke(1.5.dp, BorderWarm),
                shadowElevation = 4.dp
            ) {
                Text(
                    text = stringResource(R.string.spin_more_hint),
                    style = MaterialTheme.typography.labelLarge,
                    color = TextSecondary,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(14.dp)
                )
            }
        }

        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            contentPadding = PaddingValues(20.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(items = state.rows, key = { it.item.id }) { row ->
                RewardCard(
                    row = row,
                    onUnlock = { viewModel.unlock(row.item) }
                )
            }
        }
    }
}

@Composable
private fun RewardCard(
    row: RewardRow,
    onUnlock: () -> Unit
) {
    val ownedLabel = stringResource(R.string.state_owned)
    val lockedLabel = stringResource(R.string.state_not_enough)
    val name = stringResource(ContentRes.rewardName(row.item.id))

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .height(186.dp)
            .alpha(if (row.owned || row.affordable) 1f else 0.55f)
            .semantics {
                stateDescription = if (row.owned) ownedLabel else lockedLabel
            },
        shape = RoundedCornerShape(18.dp),
        color = SurfaceCard,
        border = BorderStroke(1.5.dp, if (row.owned) Success else BorderWarm),
        shadowElevation = 6.dp
    ) {
        Box(modifier = Modifier.fillMaxWidth()) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    painter = painterResource(drawableForKey(row.item.spriteKey)),
                    contentDescription = name,
                    contentScale = ContentScale.Fit,
                    modifier = Modifier.size(76.dp)
                )
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = name,
                    style = MaterialTheme.typography.titleSmall,
                    color = TextPrimary,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(4.dp))
                if (row.owned) {
                    Text(
                        text = stringResource(R.string.owned),
                        style = MaterialTheme.typography.labelMedium,
                        color = Success
                    )
                } else {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            painter = painterResource(R.drawable.ic_star),
                            contentDescription = null,
                            tint = DeepGold,
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.size(4.dp))
                        Text(
                            text = ContentRes.formatCount(row.item.cost),
                            style = MaterialTheme.typography.labelMedium,
                            color = DeepGold
                        )
                    }
                    Spacer(modifier = Modifier.height(6.dp))
                    SecondaryButton(
                        text = stringResource(R.string.unlock),
                        onClick = onUnlock,
                        enabled = row.affordable,
                        accent = TigerOrange,
                        height = 44.dp
                    )
                }
            }
            if (row.owned) {
                Icon(
                    painter = painterResource(R.drawable.ic_check),
                    contentDescription = null,
                    tint = Success,
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(8.dp)
                        .size(20.dp)
                )
            }
        }
    }
}
