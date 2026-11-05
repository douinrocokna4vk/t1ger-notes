package com.Telqopu0.Kitdag.domain.model

data class SpinResult(
    val grid: List<Symbol>,
    val lines: List<WinLine>,
    val basePoints: Int,
    val streakBonus: Int,
    val streak: Int
) {
    val totalPoints: Int get() = basePoints + streakBonus
    val hasWin: Boolean get() = lines.isNotEmpty()
    val tigerTriples: Int get() = lines.count { it.symbol == Symbol.TIGER }
}
