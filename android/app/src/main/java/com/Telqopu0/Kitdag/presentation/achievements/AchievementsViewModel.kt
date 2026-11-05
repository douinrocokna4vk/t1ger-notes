package com.Telqopu0.Kitdag.presentation.achievements

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.Telqopu0.Kitdag.domain.repository.ProgressRepository
import com.Telqopu0.Kitdag.domain.usecase.UpdateAchievementsUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class AchievementsViewModel(
    private val repository: ProgressRepository,
    private val buildAchievements: UpdateAchievementsUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(AchievementsUiState())
    val state: StateFlow<AchievementsUiState> = _state.asStateFlow()

    init {
        refresh()
    }

    fun refresh() {
        viewModelScope.launch {
            val items = buildAchievements.build(repository.stats())
            _state.update {
                it.copy(items = items, unlocked = items.count { entry -> entry.unlocked })
            }
        }
    }
}
