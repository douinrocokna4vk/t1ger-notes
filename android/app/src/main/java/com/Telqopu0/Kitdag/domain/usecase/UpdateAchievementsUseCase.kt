package com.Telqopu0.Kitdag.domain.usecase

import com.Telqopu0.Kitdag.domain.model.Achievement
import com.Telqopu0.Kitdag.domain.model.AchievementDefinition
import com.Telqopu0.Kitdag.domain.model.PlayerStats

class UpdateAchievementsUseCase(
    private val definitions: List<AchievementDefinition>
) {

    fun build(stats: PlayerStats): List<Achievement> = definitions.map { definition ->
        Achievement(
            id = definition.id,
            progress = progressFor(definition.id, stats).coerceAtMost(definition.goal),
            goal = definition.goal
        )
    }

    fun unlockedCount(stats: PlayerStats): Int = build(stats).count { it.unlocked }

    private fun progressFor(id: String, stats: PlayerStats): Int = when (id) {
        ID_FIRST_SPIN -> stats.totalSpins
        ID_LUCKY_THREE -> stats.lineWins
        ID_TIGER_EYE -> stats.tigerTriples
        ID_COIN_HOARDER -> stats.coinsEarned
        ID_LEVEL_FIVE -> stats.topLevel
        ID_PERFECT_ROUND -> stats.perfectRounds
        ID_HOT_STREAK -> stats.longestStreak
        ID_HUNDRED_SPINS -> stats.totalSpins
        else -> 0
    }

    companion object {
        const val ID_FIRST_SPIN = "first_spin"
        const val ID_LUCKY_THREE = "lucky_three"
        const val ID_TIGER_EYE = "tiger_eye"
        const val ID_COIN_HOARDER = "coin_hoarder"
        const val ID_LEVEL_FIVE = "level_five"
        const val ID_PERFECT_ROUND = "perfect_round"
        const val ID_HOT_STREAK = "hot_streak"
        const val ID_HUNDRED_SPINS = "hundred_spins"
    }
}
