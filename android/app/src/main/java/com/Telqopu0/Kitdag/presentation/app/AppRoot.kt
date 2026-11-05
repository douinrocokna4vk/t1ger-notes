package com.Telqopu0.Kitdag.presentation.app

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.Telqopu0.Kitdag.core.config.GameConfig
import com.Telqopu0.Kitdag.core.di.AppViewModelFactory
import com.Telqopu0.Kitdag.core.navigation.Screen
import com.Telqopu0.Kitdag.presentation.achievements.AchievementsScreen
import com.Telqopu0.Kitdag.presentation.dailybonus.DailyBonusScreen
import com.Telqopu0.Kitdag.presentation.game.GameScreen
import com.Telqopu0.Kitdag.presentation.gameover.GameOverScreen
import com.Telqopu0.Kitdag.presentation.levelmap.LevelMapScreen
import com.Telqopu0.Kitdag.presentation.menu.MenuScreen
import com.Telqopu0.Kitdag.presentation.rewards.RewardsScreen
import com.Telqopu0.Kitdag.presentation.settings.SettingsScreen
import com.Telqopu0.Kitdag.presentation.splash.SplashScreen
import com.Telqopu0.Kitdag.presentation.statistics.StatisticsScreen

@Composable
fun AppRoot(modifier: Modifier = Modifier) {
    val appViewModel: AppViewModel = viewModel(factory = AppViewModelFactory)
    val screen by appViewModel.screen.collectAsStateWithLifecycle()
    val canGoBack by appViewModel.canGoBack.collectAsStateWithLifecycle()

    BackHandler(enabled = canGoBack) {
        appViewModel.back()
    }

    AnimatedContent(
        targetState = screen,
        modifier = modifier.fillMaxSize(),
        transitionSpec = {
            fadeIn(tween(GameConfig.SCREEN_FADE_IN_MS)) togetherWith
                fadeOut(tween(GameConfig.SCREEN_FADE_OUT_MS))
        },
        label = "screen"
    ) { target ->
        when (target) {
            is Screen.Splash -> SplashScreen(
                onFinished = { appViewModel.replace(Screen.Menu) }
            )

            is Screen.Menu -> MenuScreen(
                onPlay = { level -> appViewModel.open(Screen.Game(level)) },
                onLevels = { appViewModel.open(Screen.LevelMap) },
                onBonus = { appViewModel.open(Screen.DailyBonus) },
                onRewards = { appViewModel.open(Screen.Rewards) },
                onAwards = { appViewModel.open(Screen.Achievements) },
                onStats = { appViewModel.open(Screen.Statistics) },
                onSettings = { appViewModel.open(Screen.Settings) }
            )

            is Screen.LevelMap -> LevelMapScreen(
                onBack = { appViewModel.back() },
                onLevel = { level -> appViewModel.open(Screen.Game(level)) }
            )

            is Screen.Game -> GameScreen(
                level = target.level,
                onHome = { appViewModel.openMenu() },
                onFinished = { result ->
                    appViewModel.replace(
                        Screen.GameOver(
                            level = result.level,
                            score = result.score,
                            target = result.target,
                            stars = result.stars,
                            coins = result.coins,
                            newBest = result.newBest
                        )
                    )
                }
            )

            is Screen.GameOver -> GameOverScreen(
                level = target.level,
                score = target.score,
                target = target.target,
                stars = target.stars,
                coins = target.coins,
                newBest = target.newBest,
                onPlayAgain = { level -> appViewModel.replace(Screen.Game(level)) },
                onNextLevel = { level -> appViewModel.replace(Screen.Game(level)) },
                onMenu = { appViewModel.openMenu() }
            )

            is Screen.DailyBonus -> DailyBonusScreen(
                onBack = { appViewModel.back() }
            )

            is Screen.Rewards -> RewardsScreen(
                onBack = { appViewModel.back() }
            )

            is Screen.Achievements -> AchievementsScreen(
                onBack = { appViewModel.back() }
            )

            is Screen.Statistics -> StatisticsScreen(
                onBack = { appViewModel.back() }
            )

            is Screen.Settings -> SettingsScreen(
                onBack = { appViewModel.back() },
                onReset = { appViewModel.openMenu() }
            )
        }
    }
}
