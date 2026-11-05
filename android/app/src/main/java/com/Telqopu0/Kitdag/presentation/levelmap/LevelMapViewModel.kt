package com.Telqopu0.Kitdag.presentation.levelmap

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.Telqopu0.Kitdag.domain.repository.ProgressRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class LevelMapViewModel(
    private val repository: ProgressRepository
) : ViewModel() {

    private val _state = MutableStateFlow(LevelMapUiState())
    val state: StateFlow<LevelMapUiState> = _state.asStateFlow()

    init {
        refresh()
    }

    fun refresh() {
        viewModelScope.launch {
            _state.update {
                it.copy(
                    levels = repository.levels(),
                    coins = repository.coins(),
                    currentLevel = repository.topLevel()
                )
            }
        }
    }
}
