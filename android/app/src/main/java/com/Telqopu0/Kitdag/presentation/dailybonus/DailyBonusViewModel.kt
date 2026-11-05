package com.Telqopu0.Kitdag.presentation.dailybonus

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.Telqopu0.Kitdag.data.sample.SampleData
import com.Telqopu0.Kitdag.domain.repository.ProgressRepository
import com.Telqopu0.Kitdag.domain.usecase.ClaimDailyBonusUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class DailyBonusViewModel(
    private val repository: ProgressRepository,
    private val claimBonus: ClaimDailyBonusUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(DailyBonusUiState(amounts = SampleData.dailyRewards))
    val state: StateFlow<DailyBonusUiState> = _state.asStateFlow()

    init {
        refresh()
    }

    fun refresh() {
        viewModelScope.launch {
            val now = System.currentTimeMillis()
            val bonus = repository.dailyBonus()
            _state.update {
                it.copy(
                    coins = repository.coins(),
                    claimedDays = bonus.claimedDays,
                    ready = claimBonus.readyAt(bonus, now),
                    remainingMillis = claimBonus.millisUntilReady(bonus, now),
                    amounts = SampleData.dailyRewards
                )
            }
        }
    }

    fun claim() {
        viewModelScope.launch {
            val now = System.currentTimeMillis()
            val bonus = repository.dailyBonus()
            if (!claimBonus.readyAt(bonus, now)) return@launch
            val amount = claimBonus.rewardFor(bonus.claimedDays)
            repository.saveDailyBonus(claimBonus.claim(bonus, now))
            repository.addCoins(amount)
            val updated = repository.dailyBonus()
            _state.update {
                it.copy(
                    coins = repository.coins(),
                    claimedDays = updated.claimedDays,
                    ready = claimBonus.readyAt(updated, now),
                    remainingMillis = claimBonus.millisUntilReady(updated, now),
                    justClaimed = amount
                )
            }
        }
    }
}
