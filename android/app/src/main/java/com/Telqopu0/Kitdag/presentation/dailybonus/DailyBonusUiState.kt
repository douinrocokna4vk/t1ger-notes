package com.Telqopu0.Kitdag.presentation.dailybonus

data class DailyBonusUiState(
    val coins: Int = 0,
    val claimedDays: Int = 0,
    val ready: Boolean = false,
    val remainingMillis: Long = 0L,
    val amounts: List<Int> = emptyList(),
    val justClaimed: Int = 0
)
