package com.Telqopu0.Kitdag.core.config

import androidx.compose.ui.unit.dp

object GameConfig {
    const val LOADER_DURATION_MS = 8000L
    const val SPIN_TICK_MS = 70L
    const val SPIN_DURATION_MS = 1100L
    const val COLUMN_STAGGER_MS = 240L
    const val RESULT_DELAY_MS = 600L
    const val IDLE_RETURN_MS = 900L
    const val SCREEN_FADE_IN_MS = 280
    const val SCREEN_FADE_OUT_MS = 220
    const val SPINS_PER_ROUND = 12
    const val ROWS = 3
    const val COLS = 3
    const val CELL_COUNT = 9
    const val TARGET_BASE = 220
    const val TARGET_STEP = 60
    const val LEVEL_COUNT = 12
    const val START_COINS = 100
    const val MAX_STREAK_BONUS = 0.5f
    const val STREAK_STEP = 0.1f
    const val DAILY_STREAK_DAYS = 7
    const val DAY_MILLIS = 86400000L

    val BET_MULTIPLIERS = listOf(1, 2, 3)
    val BOARD_PADDING = 8.dp
    val BOARD_BORDER = 3.dp
    val BOARD_MAX_WIDTH = 360.dp

    fun targetFor(level: Int): Int = TARGET_BASE + (level - 1) * TARGET_STEP
}
