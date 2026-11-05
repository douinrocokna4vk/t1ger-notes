package com.Telqopu0.Kitdag.presentation.game

data class GameResult(
    val level: Int,
    val score: Int,
    val target: Int,
    val stars: Int,
    val coins: Int,
    val newBest: Boolean
)
