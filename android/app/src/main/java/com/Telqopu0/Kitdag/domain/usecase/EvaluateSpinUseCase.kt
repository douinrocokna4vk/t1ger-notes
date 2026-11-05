package com.Telqopu0.Kitdag.domain.usecase

import com.Telqopu0.Kitdag.domain.model.LineKind
import com.Telqopu0.Kitdag.domain.model.SpinResult
import com.Telqopu0.Kitdag.domain.model.Symbol
import com.Telqopu0.Kitdag.domain.model.WinLine

class EvaluateSpinUseCase(
    private val maxStreakBonus: Float,
    private val streakStep: Float
) {

    fun evaluate(grid: List<Symbol>, bet: Int, previousStreak: Int): SpinResult {
        val lines = mutableListOf<WinLine>()
        for (cells in PAY_LINES) {
            val first = grid[cells[0]]
            if (grid[cells[1]] != first || grid[cells[2]] != first) continue
            val kind = kindOf(cells)
            val points = (first.points * kind.bonus * bet).toInt()
            lines.add(WinLine(cells = cells, symbol = first, kind = kind, points = points))
        }
        val base = lines.sumOf { it.points }
        val streak = if (lines.isEmpty()) 0 else previousStreak + 1
        val multiplier = if (streak < 2) 0f
        else ((streak - 1) * streakStep).coerceAtMost(maxStreakBonus)
        val bonus = (base * multiplier).toInt()
        return SpinResult(
            grid = grid,
            lines = lines,
            basePoints = base,
            streakBonus = bonus,
            streak = streak
        )
    }

    private fun kindOf(cells: List<Int>): LineKind = when {
        cells[0] % 3 == 0 && cells[1] == cells[0] + 1 -> LineKind.ROW
        cells[1] == cells[0] + 3 -> LineKind.COLUMN
        else -> LineKind.DIAGONAL
    }

    companion object {
        val PAY_LINES: List<List<Int>> = listOf(
            listOf(0, 1, 2),
            listOf(3, 4, 5),
            listOf(6, 7, 8),
            listOf(0, 3, 6),
            listOf(1, 4, 7),
            listOf(2, 5, 8),
            listOf(0, 4, 8),
            listOf(2, 4, 6)
        )
    }
}
