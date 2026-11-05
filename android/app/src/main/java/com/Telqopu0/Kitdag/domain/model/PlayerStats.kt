package com.Telqopu0.Kitdag.domain.model

data class PlayerStats(
    val totalSpins: Int = 0,
    val bestScore: Int = 0,
    val lineWins: Int = 0,
    val levelsDone: Int = 0,
    val coinsEarned: Int = 0,
    val longestStreak: Int = 0,
    val tigerTriples: Int = 0,
    val perfectRounds: Int = 0,
    val topLevel: Int = 1,
    val symbolCounts: Map<Symbol, Int> = emptyMap()
)
