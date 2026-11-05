package com.Telqopu0.Kitdag.presentation.rewards

import com.Telqopu0.Kitdag.domain.model.RewardItem

data class RewardRow(
    val item: RewardItem,
    val owned: Boolean,
    val affordable: Boolean
)

data class RewardsUiState(
    val rows: List<RewardRow> = emptyList(),
    val coins: Int = 0,
    val anyAffordable: Boolean = false
)
