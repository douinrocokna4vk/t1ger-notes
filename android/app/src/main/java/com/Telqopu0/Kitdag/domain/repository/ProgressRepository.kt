package com.Telqopu0.Kitdag.domain.repository

import com.Telqopu0.Kitdag.domain.model.AppSettings
import com.Telqopu0.Kitdag.domain.model.DailyBonusState
import com.Telqopu0.Kitdag.domain.model.LevelProgress
import com.Telqopu0.Kitdag.domain.model.PlayerStats
import com.Telqopu0.Kitdag.domain.model.RewardItem
import com.Telqopu0.Kitdag.domain.model.Symbol

interface ProgressRepository {
    fun coins(): Int
    fun addCoins(amount: Int)
    fun spendCoins(amount: Int): Boolean
    fun topLevel(): Int
    fun levels(): List<LevelProgress>
    fun levelProgress(level: Int): LevelProgress
    fun recordSpin(symbols: List<Symbol>, lineWins: Int, streak: Int, tigerTriples: Int)
    fun recordRound(level: Int, score: Int, stars: Int, coins: Int): Boolean
    fun stats(): PlayerStats
    fun unlockedRewards(): Set<String>
    fun unlockReward(item: RewardItem): Boolean
    fun settings(): AppSettings
    fun saveSettings(value: AppSettings)
    fun dailyBonus(): DailyBonusState
    fun saveDailyBonus(value: DailyBonusState)
    fun reset()
}
