package com.Telqopu0.Kitdag.presentation.menu

data class MenuUiState(
    val coins: Int = 0,
    val level: Int = 1,
    val bestScore: Int = 0,
    val bonusReady: Boolean = false,
    val loading: Boolean = true
)
