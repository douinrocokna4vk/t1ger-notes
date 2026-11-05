package com.Telqopu0.Kitdag.core.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import com.Telqopu0.Kitdag.presentation.achievements.AchievementsViewModel
import com.Telqopu0.Kitdag.presentation.app.AppViewModel
import com.Telqopu0.Kitdag.presentation.dailybonus.DailyBonusViewModel
import com.Telqopu0.Kitdag.presentation.game.GameViewModel
import com.Telqopu0.Kitdag.presentation.gameover.GameOverViewModel
import com.Telqopu0.Kitdag.presentation.levelmap.LevelMapViewModel
import com.Telqopu0.Kitdag.presentation.menu.MenuViewModel
import com.Telqopu0.Kitdag.presentation.rewards.RewardsViewModel
import com.Telqopu0.Kitdag.presentation.settings.SettingsViewModel
import com.Telqopu0.Kitdag.presentation.splash.SplashViewModel
import com.Telqopu0.Kitdag.presentation.statistics.StatisticsViewModel

object AppViewModelFactory : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val repository = ServiceLocator.progress
        val created: ViewModel = when {
            modelClass.isAssignableFrom(AppViewModel::class.java) ->
                AppViewModel()

            modelClass.isAssignableFrom(SplashViewModel::class.java) ->
                SplashViewModel()

            modelClass.isAssignableFrom(MenuViewModel::class.java) ->
                MenuViewModel(repository, ServiceLocator.claimDailyBonus)

            modelClass.isAssignableFrom(LevelMapViewModel::class.java) ->
                LevelMapViewModel(repository)

            modelClass.isAssignableFrom(GameViewModel::class.java) ->
                GameViewModel(
                    repository,
                    ServiceLocator.spinReels,
                    ServiceLocator.evaluateSpin,
                    ServiceLocator.calculateStars
                )

            modelClass.isAssignableFrom(GameOverViewModel::class.java) ->
                GameOverViewModel(repository)

            modelClass.isAssignableFrom(DailyBonusViewModel::class.java) ->
                DailyBonusViewModel(repository, ServiceLocator.claimDailyBonus)

            modelClass.isAssignableFrom(RewardsViewModel::class.java) ->
                RewardsViewModel(repository)

            modelClass.isAssignableFrom(AchievementsViewModel::class.java) ->
                AchievementsViewModel(repository, ServiceLocator.updateAchievements)

            modelClass.isAssignableFrom(StatisticsViewModel::class.java) ->
                StatisticsViewModel(repository, ServiceLocator.updateAchievements)

            modelClass.isAssignableFrom(SettingsViewModel::class.java) ->
                SettingsViewModel(repository)

            else -> throw IllegalArgumentException(modelClass.name)
        }
        @Suppress("UNCHECKED_CAST")
        return created as T
    }

    override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T =
        create(modelClass)
}
