package com.Telqopu0.Kitdag.presentation.achievements

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.Telqopu0.Kitdag.R
import com.Telqopu0.Kitdag.core.di.AppViewModelFactory
import com.Telqopu0.Kitdag.core.theme.AwardsOverlay
import com.Telqopu0.Kitdag.core.theme.BorderWarm
import com.Telqopu0.Kitdag.core.theme.DeepGold
import com.Telqopu0.Kitdag.core.theme.LuckyGold
import com.Telqopu0.Kitdag.core.theme.SurfaceCard
import com.Telqopu0.Kitdag.core.theme.SurfaceMuted
import com.Telqopu0.Kitdag.core.theme.TextMuted
import com.Telqopu0.Kitdag.core.theme.TextPrimary
import com.Telqopu0.Kitdag.core.theme.TextSecondary
import com.Telqopu0.Kitdag.core.theme.TigerOrange
import com.Telqopu0.Kitdag.core.ui.ScreenScaffold
import com.Telqopu0.Kitdag.domain.model.Achievement
import com.Telqopu0.Kitdag.presentation.common.ContentRes

@Composable
fun AchievementsScreen(
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: AchievementsViewModel = viewModel(factory = AppViewModelFactory)
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.refresh()
    }

    ScreenScaffold(
        title = stringResource(R.string.achievements_title),
        backDescription = stringResource(R.string.cd_back),
        onBack = onBack,
        overlay = AwardsOverlay,
        modifier = modifier
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            contentPadding = PaddingValues(horizontal = 20.dp, vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            items(items = state.items, key = { it.id }) { item ->
                AchievementRow(item = item)
            }
        }
    }
}

@Composable
private fun AchievementRow(item: Achievement) {
    val unlockedLabel = stringResource(R.string.state_unlocked)
    val lockedLabel = stringResource(R.string.state_locked)
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .height(88.dp)
            .semantics {
                stateDescription = if (item.unlocked) unlockedLabel else lockedLabel
            },
        shape = RoundedCornerShape(16.dp),
        color = SurfaceCard,
        border = BorderStroke(1.5.dp, if (item.unlocked) LuckyGold else BorderWarm),
        shadowElevation = 4.dp
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(
                        if (item.unlocked) {
                            Brush.verticalGradient(listOf(LuckyGold, DeepGold))
                        } else {
                            Brush.verticalGradient(listOf(SurfaceMuted, SurfaceMuted))
                        }
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_star),
                    contentDescription = null,
                    tint = if (item.unlocked) TextPrimary else TextMuted,
                    modifier = Modifier.size(24.dp)
                )
            }

            Spacer(modifier = Modifier.size(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = stringResource(ContentRes.achievementTitle(item.id)),
                    style = MaterialTheme.typography.titleSmall,
                    color = TextPrimary
                )
                Text(
                    text = stringResource(ContentRes.achievementDescription(item.id)),
                    style = MaterialTheme.typography.bodySmall,
                    color = TextSecondary
                )
                Spacer(modifier = Modifier.height(6.dp))
                LinearProgressIndicator(
                    progress = { item.fraction },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(6.dp)
                        .clip(RoundedCornerShape(3.dp)),
                    color = TigerOrange,
                    trackColor = SurfaceMuted
                )
            }

            Spacer(modifier = Modifier.size(10.dp))

            Text(
                text = stringResource(R.string.counter_format, item.progress, item.goal),
                style = MaterialTheme.typography.labelMedium,
                color = TextSecondary
            )
        }
    }
}
