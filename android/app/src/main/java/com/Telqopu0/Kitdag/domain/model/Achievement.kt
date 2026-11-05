package com.Telqopu0.Kitdag.domain.model

data class AchievementDefinition(
    val id: String,
    val goal: Int
)

data class Achievement(
    val id: String,
    val progress: Int,
    val goal: Int
) {
    val unlocked: Boolean get() = progress >= goal
    val fraction: Float get() = if (goal <= 0) 0f else (progress.toFloat() / goal).coerceIn(0f, 1f)
}
