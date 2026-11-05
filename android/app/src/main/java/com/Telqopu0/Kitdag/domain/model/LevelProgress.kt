package com.Telqopu0.Kitdag.domain.model

data class LevelProgress(
    val level: Int,
    val target: Int,
    val stars: Int,
    val bestScore: Int,
    val unlocked: Boolean
)
