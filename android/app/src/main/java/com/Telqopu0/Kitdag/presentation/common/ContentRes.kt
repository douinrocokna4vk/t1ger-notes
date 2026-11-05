package com.Telqopu0.Kitdag.presentation.common

import com.Telqopu0.Kitdag.R
import com.Telqopu0.Kitdag.domain.model.Symbol
import com.Telqopu0.Kitdag.domain.usecase.UpdateAchievementsUseCase
import java.util.Locale

object ContentRes {

    fun symbolName(symbol: Symbol): Int = when (symbol) {
        Symbol.TIGER -> R.string.symbol_tiger
        Symbol.INGOT -> R.string.symbol_ingot
        Symbol.COIN -> R.string.symbol_coin
        Symbol.LANTERN -> R.string.symbol_lantern
        Symbol.BAMBOO -> R.string.symbol_bamboo
    }

    fun rewardName(id: String): Int = when (id) {
        "jade_lantern" -> R.string.reward_jade_lantern
        "lucky_coin" -> R.string.reward_lucky_coin
        "bamboo_charm" -> R.string.reward_bamboo_charm
        "gold_ingot" -> R.string.reward_gold_ingot
        "tiger_eye" -> R.string.reward_tiger_eye
        else -> R.string.reward_golden_cabinet
    }

    fun achievementTitle(id: String): Int = when (id) {
        UpdateAchievementsUseCase.ID_FIRST_SPIN -> R.string.award_first_spin
        UpdateAchievementsUseCase.ID_LUCKY_THREE -> R.string.award_lucky_three
        UpdateAchievementsUseCase.ID_TIGER_EYE -> R.string.award_tiger_eye
        UpdateAchievementsUseCase.ID_COIN_HOARDER -> R.string.award_coin_hoarder
        UpdateAchievementsUseCase.ID_LEVEL_FIVE -> R.string.award_level_five
        UpdateAchievementsUseCase.ID_PERFECT_ROUND -> R.string.award_perfect_round
        UpdateAchievementsUseCase.ID_HOT_STREAK -> R.string.award_hot_streak
        else -> R.string.award_hundred_spins
    }

    fun achievementDescription(id: String): Int = when (id) {
        UpdateAchievementsUseCase.ID_FIRST_SPIN -> R.string.award_first_spin_desc
        UpdateAchievementsUseCase.ID_LUCKY_THREE -> R.string.award_lucky_three_desc
        UpdateAchievementsUseCase.ID_TIGER_EYE -> R.string.award_tiger_eye_desc
        UpdateAchievementsUseCase.ID_COIN_HOARDER -> R.string.award_coin_hoarder_desc
        UpdateAchievementsUseCase.ID_LEVEL_FIVE -> R.string.award_level_five_desc
        UpdateAchievementsUseCase.ID_PERFECT_ROUND -> R.string.award_perfect_round_desc
        UpdateAchievementsUseCase.ID_HOT_STREAK -> R.string.award_hot_streak_desc
        else -> R.string.award_hundred_spins_desc
    }

    fun formatCount(value: Int): String = String.format(Locale.US, "%,d", value)

    fun formatDuration(millis: Long): String {
        val totalMinutes = (millis / 60000L).coerceAtLeast(0L)
        val hours = totalMinutes / 60
        val minutes = totalMinutes % 60
        return String.format(Locale.US, "%02d:%02d", hours, minutes)
    }
}
