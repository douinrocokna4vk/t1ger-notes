package com.Telqopu0.Kitdag.data.repository

import com.Telqopu0.Kitdag.core.config.GameConfig
import com.Telqopu0.Kitdag.data.local.PreferencesStorage
import com.Telqopu0.Kitdag.domain.model.AppSettings
import com.Telqopu0.Kitdag.domain.model.DailyBonusState
import com.Telqopu0.Kitdag.domain.model.LevelProgress
import com.Telqopu0.Kitdag.domain.model.PlayerStats
import com.Telqopu0.Kitdag.domain.model.RewardItem
import com.Telqopu0.Kitdag.domain.model.Symbol
import com.Telqopu0.Kitdag.domain.repository.ProgressRepository

class ProgressRepositoryImpl(
    private val storage: PreferencesStorage
) : ProgressRepository {

    override fun coins(): Int = storage.readInt(KEY_COINS, GameConfig.START_COINS)

    override fun addCoins(amount: Int) {
        if (amount <= 0) return
        storage.writeInt(KEY_COINS, coins() + amount)
        storage.writeInt(KEY_COINS_EARNED, storage.readInt(KEY_COINS_EARNED, 0) + amount)
    }

    override fun spendCoins(amount: Int): Boolean {
        val balance = coins()
        if (amount <= 0 || balance < amount) return false
        storage.writeInt(KEY_COINS, balance - amount)
        return true
    }

    override fun topLevel(): Int =
        storage.readInt(KEY_TOP_LEVEL, 1).coerceIn(1, GameConfig.LEVEL_COUNT)

    override fun levels(): List<LevelProgress> =
        (1..GameConfig.LEVEL_COUNT).map { levelProgress(it) }

    override fun levelProgress(level: Int): LevelProgress = LevelProgress(
        level = level,
        target = GameConfig.targetFor(level),
        stars = storage.readInt(KEY_STARS + level, 0),
        bestScore = storage.readInt(KEY_LEVEL_BEST + level, 0),
        unlocked = level <= topLevel()
    )

    override fun recordSpin(symbols: List<Symbol>, lineWins: Int, streak: Int, tigerTriples: Int) {
        storage.writeInt(KEY_TOTAL_SPINS, storage.readInt(KEY_TOTAL_SPINS, 0) + 1)
        if (lineWins > 0) {
            storage.writeInt(KEY_LINE_WINS, storage.readInt(KEY_LINE_WINS, 0) + lineWins)
        }
        if (tigerTriples > 0) {
            storage.writeInt(KEY_TIGERS, storage.readInt(KEY_TIGERS, 0) + tigerTriples)
        }
        if (streak > storage.readInt(KEY_LONGEST_STREAK, 0)) {
            storage.writeInt(KEY_LONGEST_STREAK, streak)
        }
        for (symbol in symbols) {
            val key = KEY_SYMBOL + symbol.name
            storage.writeInt(key, storage.readInt(key, 0) + 1)
        }
    }

    override fun recordRound(level: Int, score: Int, stars: Int, coins: Int): Boolean {
        val bestKey = KEY_LEVEL_BEST + level
        val previousBest = storage.readInt(bestKey, 0)
        val newBest = score > previousBest
        if (newBest) storage.writeInt(bestKey, score)
        if (score > storage.readInt(KEY_BEST_SCORE, 0)) storage.writeInt(KEY_BEST_SCORE, score)

        val starsKey = KEY_STARS + level
        val previousStars = storage.readInt(starsKey, 0)
        if (stars > previousStars) {
            storage.writeInt(starsKey, stars)
            if (previousStars == 0 && stars > 0) {
                storage.writeInt(KEY_LEVELS_DONE, storage.readInt(KEY_LEVELS_DONE, 0) + 1)
            }
        }
        if (stars >= 3) {
            storage.writeInt(KEY_PERFECT_ROUNDS, storage.readInt(KEY_PERFECT_ROUNDS, 0) + 1)
        }
        if (stars > 0 && level >= topLevel() && level < GameConfig.LEVEL_COUNT) {
            storage.writeInt(KEY_TOP_LEVEL, level + 1)
        }
        addCoins(coins)
        return newBest
    }

    override fun stats(): PlayerStats = PlayerStats(
        totalSpins = storage.readInt(KEY_TOTAL_SPINS, 0),
        bestScore = storage.readInt(KEY_BEST_SCORE, 0),
        lineWins = storage.readInt(KEY_LINE_WINS, 0),
        levelsDone = storage.readInt(KEY_LEVELS_DONE, 0),
        coinsEarned = storage.readInt(KEY_COINS_EARNED, 0),
        longestStreak = storage.readInt(KEY_LONGEST_STREAK, 0),
        tigerTriples = storage.readInt(KEY_TIGERS, 0),
        perfectRounds = storage.readInt(KEY_PERFECT_ROUNDS, 0),
        topLevel = topLevel(),
        symbolCounts = Symbol.entries.associateWith { storage.readInt(KEY_SYMBOL + it.name, 0) }
    )

    override fun unlockedRewards(): Set<String> = storage.readStringSet(KEY_REWARDS)

    override fun unlockReward(item: RewardItem): Boolean {
        val owned = unlockedRewards()
        if (owned.contains(item.id)) return false
        if (!spendCoins(item.cost)) return false
        storage.writeStringSet(KEY_REWARDS, owned + item.id)
        return true
    }

    override fun settings(): AppSettings = AppSettings(
        sound = storage.readBoolean(KEY_SOUND, true),
        vibration = storage.readBoolean(KEY_VIBRATION, true),
        fastAnimations = storage.readBoolean(KEY_FAST, false)
    )

    override fun saveSettings(value: AppSettings) {
        storage.writeBoolean(KEY_SOUND, value.sound)
        storage.writeBoolean(KEY_VIBRATION, value.vibration)
        storage.writeBoolean(KEY_FAST, value.fastAnimations)
    }

    override fun dailyBonus(): DailyBonusState = DailyBonusState(
        claimedDays = storage.readInt(KEY_BONUS_DAYS, 0),
        lastClaimMillis = storage.readLong(KEY_BONUS_TIME, 0L)
    )

    override fun saveDailyBonus(value: DailyBonusState) {
        storage.writeInt(KEY_BONUS_DAYS, value.claimedDays)
        storage.writeLong(KEY_BONUS_TIME, value.lastClaimMillis)
    }

    override fun reset() {
        storage.clearAll()
    }

    private companion object {
        const val KEY_COINS = "coins"
        const val KEY_COINS_EARNED = "coins_earned"
        const val KEY_TOP_LEVEL = "top_level"
        const val KEY_STARS = "stars_"
        const val KEY_LEVEL_BEST = "level_best_"
        const val KEY_BEST_SCORE = "best_score"
        const val KEY_TOTAL_SPINS = "total_spins"
        const val KEY_LINE_WINS = "line_wins"
        const val KEY_LEVELS_DONE = "levels_done"
        const val KEY_LONGEST_STREAK = "longest_streak"
        const val KEY_TIGERS = "tiger_triples"
        const val KEY_PERFECT_ROUNDS = "perfect_rounds"
        const val KEY_SYMBOL = "symbol_"
        const val KEY_REWARDS = "rewards"
        const val KEY_SOUND = "sound"
        const val KEY_VIBRATION = "vibration"
        const val KEY_FAST = "fast_animations"
        const val KEY_BONUS_DAYS = "bonus_days"
        const val KEY_BONUS_TIME = "bonus_time"
    }
}
