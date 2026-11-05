package com.Telqopu0.Kitdag.core.ui

import com.Telqopu0.Kitdag.R
import com.Telqopu0.Kitdag.domain.model.Symbol

fun drawableForKey(key: String): Int = when (key) {
    "sprite_1" -> R.drawable.sprite_1
    "sprite_2" -> R.drawable.sprite_2
    "sprite_3" -> R.drawable.sprite_3
    "sprite_4" -> R.drawable.sprite_4
    "sprite_5" -> R.drawable.sprite_5
    "icon_home" -> R.drawable.icon_home
    "icon_pause" -> R.drawable.icon_pause
    "bg_game" -> R.drawable.bg_game
    else -> R.drawable.logo
}

fun drawableForSymbol(symbol: Symbol): Int = drawableForKey(symbol.key)
