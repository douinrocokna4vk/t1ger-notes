package com.Telqopu0.Kitdag.presentation.levelmap

import com.Telqopu0.Kitdag.domain.model.LevelProgress

data class LevelMapUiState(
    val levels: List<LevelProgress> = emptyList(),
    val coins: Int = 0,
    val currentLevel: Int = 1
)
