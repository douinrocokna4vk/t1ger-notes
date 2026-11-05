package com.Telqopu0.Kitdag.presentation.achievements

import com.Telqopu0.Kitdag.domain.model.Achievement

data class AchievementsUiState(
    val items: List<Achievement> = emptyList(),
    val unlocked: Int = 0
)
