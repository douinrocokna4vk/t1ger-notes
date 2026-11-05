package com.Telqopu0.Kitdag.presentation.menu

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.Telqopu0.Kitdag.domain.repository.ProgressRepository
import com.Telqopu0.Kitdag.domain.usecase.ClaimDailyBonusUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class MenuViewModel(
    private val repository: ProgressRepository,
    private val dailyBonus: ClaimDailyBonusUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(MenuUiState())
    val state: StateFlow<MenuUiState> = _state.asStateFlow()

    init {
        refresh()
    }

    fun refresh() {
        viewModelScope.launch {
            val stats = repository.stats()
            val ready = dailyBonus.readyAt(repository.dailyBonus(), System.currentTimeMillis())
            _state.update {
                it.copy(
                    coins = repository.coins(),
                    level = repository.topLevel(),
                    bestScore = stats.bestScore,
                    bonusReady = ready,
                    loading = false
                )
            }
        }
    }
}
