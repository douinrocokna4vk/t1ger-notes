package com.Telqopu0.Kitdag.presentation.statistics

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.Telqopu0.Kitdag.domain.model.Symbol
import com.Telqopu0.Kitdag.domain.repository.ProgressRepository
import com.Telqopu0.Kitdag.domain.usecase.UpdateAchievementsUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class StatisticsViewModel(
    private val repository: ProgressRepository,
    private val buildAchievements: UpdateAchievementsUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(StatisticsUiState())
    val state: StateFlow<StatisticsUiState> = _state.asStateFlow()

    init {
        refresh()
    }

    fun refresh() {
        viewModelScope.launch {
            val stats = repository.stats()
            val peak = Symbol.entries.maxOfOrNull { stats.symbolCounts[it] ?: 0 } ?: 0
            val shares = Symbol.entries.map { symbol ->
                val count = stats.symbolCounts[symbol] ?: 0
                SymbolShare(
                    symbol = symbol,
                    count = count,
                    fraction = if (peak <= 0) 0f else count.toFloat() / peak
                )
            }
            _state.update {
                it.copy(
                    stats = stats,
                    shares = shares,
                    awardsUnlocked = buildAchievements.unlockedCount(stats)
                )
            }
        }
    }
}
