package com.Telqopu0.Kitdag.domain.usecase

import com.Telqopu0.Kitdag.domain.model.Symbol
import kotlin.random.Random

class SpinReelsUseCase {

    fun roll(cells: Int, random: Random = Random.Default): List<Symbol> =
        List(cells) { rollOne(random) }

    fun rollOne(random: Random = Random.Default): Symbol {
        var ticket = random.nextInt(Symbol.totalWeight)
        for (symbol in Symbol.entries) {
            ticket -= symbol.weight
            if (ticket < 0) return symbol
        }
        return Symbol.BAMBOO
    }
}
