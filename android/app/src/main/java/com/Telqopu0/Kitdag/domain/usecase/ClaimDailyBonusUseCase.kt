package com.Telqopu0.Kitdag.domain.usecase

import com.Telqopu0.Kitdag.domain.model.DailyBonusState

class ClaimDailyBonusUseCase(
    private val rewards: List<Int>,
    private val dayMillis: Long
) {

    fun readyAt(state: DailyBonusState, nowMillis: Long): Boolean {
        if (state.claimedDays >= rewards.size) return false
        if (state.lastClaimMillis <= 0L) return true
        return nowMillis - state.lastClaimMillis >= dayMillis
    }

    fun millisUntilReady(state: DailyBonusState, nowMillis: Long): Long {
        if (state.lastClaimMillis <= 0L) return 0L
        val elapsed = nowMillis - state.lastClaimMillis
        return (dayMillis - elapsed).coerceAtLeast(0L)
    }

    fun rewardFor(dayIndex: Int): Int = rewards.getOrElse(dayIndex) { rewards.last() }

    fun claim(state: DailyBonusState, nowMillis: Long): DailyBonusState =
        DailyBonusState(
            claimedDays = (state.claimedDays + 1).coerceAtMost(rewards.size),
            lastClaimMillis = nowMillis
        )
}
