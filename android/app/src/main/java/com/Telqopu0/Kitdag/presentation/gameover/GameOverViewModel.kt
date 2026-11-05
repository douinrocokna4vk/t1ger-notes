package com.Telqopu0.Kitdag.presentation.gameover

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.Telqopu0.Kitdag.core.config.GameConfig
import com.Telqopu0.Kitdag.domain.repository.ProgressRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class GameOverUiState(
    val coins: Int = 0,
    val bestScore: Int = 0,
    val nextLevel: Int = 0
)

class GameOverViewModel(
    private val repository: ProgressRepository
) : ViewModel() {

    private val _state = MutableStateFlow(GameOverUiState())
    val state: StateFlow<GameOverUiState> = _state.asStateFlow()

    fun load(level: Int) {
        viewModelScope.launch {
            val unlocked = repository.topLevel()
            val next = if (level < GameConfig.LEVEL_COUNT && level + 1 <= unlocked) level + 1 else 0
            _state.update {
                it.copy(
                    coins = repository.coins(),
                    bestScore = repository.levelProgress(level).bestScore,
                    nextLevel = next
                )
            }
        }
    }
}
