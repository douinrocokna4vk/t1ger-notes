package com.Telqopu0.Kitdag.presentation.game

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.Telqopu0.Kitdag.core.config.GameConfig
import com.Telqopu0.Kitdag.domain.model.Symbol
import com.Telqopu0.Kitdag.domain.repository.ProgressRepository
import com.Telqopu0.Kitdag.domain.usecase.CalculateStarsUseCase
import com.Telqopu0.Kitdag.domain.usecase.EvaluateSpinUseCase
import com.Telqopu0.Kitdag.domain.usecase.SpinReelsUseCase
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

class GameViewModel(
    private val repository: ProgressRepository,
    private val spinReels: SpinReelsUseCase,
    private val evaluateSpin: EvaluateSpinUseCase,
    private val calculateStars: CalculateStarsUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(GameUiState())
    val state: StateFlow<GameUiState> = _state.asStateFlow()

    private val _events = Channel<GameEvent>(Channel.BUFFERED)
    val events: Flow<GameEvent> = _events.receiveAsFlow()

    private var spinJob: Job? = null
    private var idleJob: Job? = null
    private var activeLevel = 0
    private var pendingGrid: List<Symbol>? = null
    private var pendingCost = 1

    fun startLevel(level: Int) {
        val current = _state.value
        if (activeLevel == level && !current.finished && current.hasSpun) return
        activeLevel = level
        spinJob?.cancel()
        idleJob?.cancel()
        pendingGrid = null
        _state.value = GameUiState(
            level = level,
            target = GameConfig.targetFor(level),
            grid = spinReels.roll(GameConfig.CELL_COUNT),
            fastAnimations = repository.settings().fastAnimations
        )
    }

    fun setBet(value: Int) {
        val current = _state.value
        if (current.spinning || current.finished) return
        if (!GameConfig.BET_MULTIPLIERS.contains(value)) return
        _state.update { it.copy(bet = value) }
    }

    fun spin() {
        val current = _state.value
        if (current.spinning || current.finished || current.spinsLeft <= 0) return
        idleJob?.cancel()
        val finalGrid = spinReels.roll(GameConfig.CELL_COUNT)
        val cost = current.bet.coerceAtMost(current.spinsLeft)
        pendingGrid = finalGrid
        pendingCost = cost
        _state.update {
            it.copy(
                spinning = true,
                hasSpun = true,
                winningCells = emptySet(),
                lastGain = 0
            )
        }
        val fast = current.fastAnimations
        val base = if (fast) GameConfig.SPIN_DURATION_MS / 2 else GameConfig.SPIN_DURATION_MS
        val stagger = if (fast) GameConfig.COLUMN_STAGGER_MS / 2 else GameConfig.COLUMN_STAGGER_MS
        spinJob = viewModelScope.launch {
            var elapsed = 0L
            var settled = 0
            while (isActive && settled < GameConfig.COLS) {
                delay(GameConfig.SPIN_TICK_MS)
                elapsed += GameConfig.SPIN_TICK_MS
                var done = 0
                for (column in 0 until GameConfig.COLS) {
                    if (elapsed >= base + stagger * column) done += 1
                }
                settled = done
                val mixed = mixGrid(finalGrid, settled)
                _state.update { it.copy(grid = mixed) }
            }
            settle()
        }
    }

    fun showPause() {
        if (_state.value.finished) return
        spinJob?.cancel()
        spinJob = null
        settle()
        _state.update { it.copy(pauseVisible = true) }
    }

    fun hidePause() {
        _state.update { it.copy(pauseVisible = false) }
    }

    fun pause() {
        spinJob?.cancel()
        spinJob = null
        idleJob?.cancel()
        idleJob = null
        settle()
    }

    private fun settle() {
        val finalGrid = pendingGrid ?: return
        if (!_state.value.spinning) return
        pendingGrid = null
        val snapshot = _state.value
        val result = evaluateSpin.evaluate(finalGrid, snapshot.bet, snapshot.streak)
        val score = snapshot.score + result.totalPoints
        val spinsLeft = (snapshot.spinsLeft - pendingCost).coerceAtLeast(0)
        val highlighted = mutableSetOf<Int>()
        for (line in result.lines) highlighted.addAll(line.cells)
        repository.recordSpin(finalGrid, result.lines.size, result.streak, result.tigerTriples)
        _state.update {
            it.copy(
                grid = finalGrid,
                spinning = false,
                score = score,
                spinsLeft = spinsLeft,
                winningCells = highlighted,
                lastGain = result.totalPoints,
                streak = result.streak
            )
        }
        val beaten = score >= (snapshot.target * 1.9f).toInt()
        if (spinsLeft <= 0 || beaten) {
            finishRound()
        } else {
            idleJob = viewModelScope.launch {
                delay(GameConfig.IDLE_RETURN_MS)
                _state.update { it.copy(winningCells = emptySet(), lastGain = 0) }
            }
        }
    }

    private fun finishRound() {
        viewModelScope.launch {
            delay(GameConfig.RESULT_DELAY_MS)
            val snapshot = _state.value
            if (snapshot.finished) return@launch
            val stars = calculateStars.stars(snapshot.score, snapshot.target)
            val coins = calculateStars.coins(snapshot.score, stars)
            val newBest = repository.recordRound(snapshot.level, snapshot.score, stars, coins)
            _state.update { it.copy(finished = true, spinning = false, pauseVisible = false) }
            _events.send(
                GameEvent.Finished(
                    GameResult(
                        level = snapshot.level,
                        score = snapshot.score,
                        target = snapshot.target,
                        stars = stars,
                        coins = coins,
                        newBest = newBest
                    )
                )
            )
        }
    }

    private fun mixGrid(finalGrid: List<Symbol>, settledColumns: Int): List<Symbol> =
        List(GameConfig.CELL_COUNT) { index ->
            val column = index % GameConfig.COLS
            if (column < settledColumns) finalGrid[index] else spinReels.rollOne()
        }
}
