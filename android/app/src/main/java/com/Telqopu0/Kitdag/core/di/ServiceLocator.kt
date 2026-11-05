package com.Telqopu0.Kitdag.core.di

import android.content.Context
import com.Telqopu0.Kitdag.core.config.GameConfig
import com.Telqopu0.Kitdag.data.local.PreferencesStorage
import com.Telqopu0.Kitdag.data.repository.ProgressRepositoryImpl
import com.Telqopu0.Kitdag.data.sample.SampleData
import com.Telqopu0.Kitdag.domain.repository.ProgressRepository
import com.Telqopu0.Kitdag.domain.usecase.CalculateStarsUseCase
import com.Telqopu0.Kitdag.domain.usecase.ClaimDailyBonusUseCase
import com.Telqopu0.Kitdag.domain.usecase.EvaluateSpinUseCase
import com.Telqopu0.Kitdag.domain.usecase.SpinReelsUseCase
import com.Telqopu0.Kitdag.domain.usecase.UpdateAchievementsUseCase

object ServiceLocator {

    private var repositoryRef: ProgressRepository? = null

    val spinReels: SpinReelsUseCase = SpinReelsUseCase()

    val evaluateSpin: EvaluateSpinUseCase = EvaluateSpinUseCase(
        maxStreakBonus = GameConfig.MAX_STREAK_BONUS,
        streakStep = GameConfig.STREAK_STEP
    )

    val calculateStars: CalculateStarsUseCase = CalculateStarsUseCase()

    val claimDailyBonus: ClaimDailyBonusUseCase = ClaimDailyBonusUseCase(
        rewards = SampleData.dailyRewards,
        dayMillis = GameConfig.DAY_MILLIS
    )

    val updateAchievements: UpdateAchievementsUseCase =
        UpdateAchievementsUseCase(SampleData.achievements)

    val progress: ProgressRepository
        get() = requireNotNull(repositoryRef)

    fun init(context: Context) {
        if (repositoryRef == null) {
            repositoryRef = ProgressRepositoryImpl(PreferencesStorage(context.applicationContext))
        }
    }
}
