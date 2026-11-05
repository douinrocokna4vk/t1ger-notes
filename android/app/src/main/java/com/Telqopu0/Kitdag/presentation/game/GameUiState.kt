package com.Telqopu0.Kitdag.presentation.game

import com.Telqopu0.Kitdag.core.config.GameConfig
import com.Telqopu0.Kitdag.domain.model.Symbol

data class GameUiState(
    val level: Int = 1,
    val target: Int = GameConfig.TARGET_BASE,
    val grid: List<Symbol> = List(GameConfig.CELL_COUNT) { Symbol.LANTERN },
    val spinsLeft: Int = GameConfig.SPINS_PER_ROUND,
    val score: Int = 0,
    val bet: Int = 1,
    val spinning: Boolean = false,
    val hasSpun: Boolean = false,
    val winningCells: Set<Int> = emptySet(),
    val lastGain: Int = 0,
    val streak: Int = 0,
    val finished: Boolean = false,
    val pauseVisible: Boolean = false,
    val fastAnimations: Boolean = false
)

sealed interface GameEvent {
    data class Finished(val result: GameResult) : GameEvent
}
