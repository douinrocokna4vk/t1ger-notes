package com.Telqopu0.Kitdag.presentation.statistics

import com.Telqopu0.Kitdag.domain.model.PlayerStats
import com.Telqopu0.Kitdag.domain.model.Symbol

data class SymbolShare(
    val symbol: Symbol,
    val count: Int,
    val fraction: Float
)

data class StatisticsUiState(
    val stats: PlayerStats = PlayerStats(),
    val shares: List<SymbolShare> = emptyList(),
    val awardsUnlocked: Int = 0
)
