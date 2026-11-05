package com.Telqopu0.Kitdag.core.navigation

sealed interface Screen {
    data object Splash : Screen
    data object Menu : Screen
    data object LevelMap : Screen
    data class Game(val level: Int) : Screen
    data class GameOver(
        val level: Int,
        val score: Int,
        val target: Int,
        val stars: Int,
        val coins: Int,
        val newBest: Boolean
    ) : Screen
    data object DailyBonus : Screen
    data object Rewards : Screen
    data object Achievements : Screen
    data object Statistics : Screen
    data object Settings : Screen
}
