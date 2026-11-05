package com.Telqopu0.Kitdag.presentation.rewards

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.Telqopu0.Kitdag.data.sample.SampleData
import com.Telqopu0.Kitdag.domain.model.RewardItem
import com.Telqopu0.Kitdag.domain.repository.ProgressRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class RewardsViewModel(
    private val repository: ProgressRepository
) : ViewModel() {

    private val _state = MutableStateFlow(RewardsUiState())
    val state: StateFlow<RewardsUiState> = _state.asStateFlow()

    init {
        refresh()
    }

    fun refresh() {
        viewModelScope.launch {
            publish()
        }
    }

    fun unlock(item: RewardItem) {
        viewModelScope.launch {
            if (repository.unlockReward(item)) publish()
        }
    }

    private fun publish() {
        val coins = repository.coins()
        val owned = repository.unlockedRewards()
        val rows = SampleData.rewards.map { item ->
            RewardRow(
                item = item,
                owned = owned.contains(item.id),
                affordable = !owned.contains(item.id) && coins >= item.cost
            )
        }
        _state.update {
            it.copy(
                rows = rows,
                coins = coins,
                anyAffordable = rows.any { row -> row.affordable }
            )
        }
    }
}
