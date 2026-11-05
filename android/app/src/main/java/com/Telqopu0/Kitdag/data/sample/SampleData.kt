package com.Telqopu0.Kitdag.data.sample

import com.Telqopu0.Kitdag.core.config.GameConfig
import com.Telqopu0.Kitdag.domain.model.AchievementDefinition
import com.Telqopu0.Kitdag.domain.model.RewardItem
import com.Telqopu0.Kitdag.domain.usecase.UpdateAchievementsUseCase

object SampleData {

    val dailyRewards: List<Int> = listOf(20, 30, 45, 60, 80, 110, 150)

    val rewards: List<RewardItem> = listOf(
        RewardItem(id = "jade_lantern", cost = 150, spriteKey = "sprite_4"),
        RewardItem(id = "lucky_coin", cost = 250, spriteKey = "sprite_3"),
        RewardItem(id = "bamboo_charm", cost = 400, spriteKey = "sprite_5"),
        RewardItem(id = "gold_ingot", cost = 600, spriteKey = "sprite_2"),
        RewardItem(id = "tiger_eye", cost = 850, spriteKey = "sprite_1"),
        RewardItem(id = "golden_cabinet", cost = 1200, spriteKey = "logo")
    )

    val achievements: List<AchievementDefinition> = listOf(
        AchievementDefinition(UpdateAchievementsUseCase.ID_FIRST_SPIN, 1),
        AchievementDefinition(UpdateAchievementsUseCase.ID_LUCKY_THREE, 3),
        AchievementDefinition(UpdateAchievementsUseCase.ID_TIGER_EYE, 1),
        AchievementDefinition(UpdateAchievementsUseCase.ID_COIN_HOARDER, 500),
        AchievementDefinition(UpdateAchievementsUseCase.ID_LEVEL_FIVE, 5),
        AchievementDefinition(UpdateAchievementsUseCase.ID_PERFECT_ROUND, 1),
        AchievementDefinition(UpdateAchievementsUseCase.ID_HOT_STREAK, 3),
        AchievementDefinition(UpdateAchievementsUseCase.ID_HUNDRED_SPINS, 100)
    )

    val levelTargets: List<Int> =
        (1..GameConfig.LEVEL_COUNT).map { GameConfig.targetFor(it) }
}
