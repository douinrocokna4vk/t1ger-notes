package com.Telqopu0.Kitdag.domain.usecase

class CalculateStarsUseCase {

    fun stars(score: Int, target: Int): Int = when {
        target <= 0 -> 0
        score >= target * 1.9f -> 3
        score >= target * 1.4f -> 2
        score >= target -> 1
        else -> 0
    }

    fun coins(score: Int, stars: Int): Int = score / 10 + 20 * stars
}
