package com.Telqopu0.Kitdag.domain.model

enum class Symbol(val key: String, val points: Int, val weight: Int) {
    TIGER("sprite_1", 100, 6),
    INGOT("sprite_2", 50, 10),
    COIN("sprite_3", 30, 16),
    LANTERN("sprite_4", 20, 22),
    BAMBOO("sprite_5", 10, 26);

    companion object {
        val totalWeight: Int
            get() = entries.sumOf { it.weight }
    }
}
