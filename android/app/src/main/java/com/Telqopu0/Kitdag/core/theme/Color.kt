package com.Telqopu0.Kitdag.core.theme

import androidx.compose.ui.graphics.Color

const val PRESET_NAME = "WARM_EARTHY"

val BgCream = Color(0xFFFFF0D5)
val BgSand = Color(0xFFF6DFB4)
val SurfaceCard = Color(0xFFFFFBF2)
val SurfaceAlt = Color(0xFFF2E2C2)
val SurfaceMuted = Color(0xFFE8DDC4)
val BorderWarm = Color(0xFFE0C79A)
val TigerOrange = Color(0xFFE85D04)
val AmberOrange = Color(0xFFF48C06)
val LuckyGold = Color(0xFFFFCA3A)
val DeepGold = Color(0xFFE8A317)
val PaleGold = Color(0xFFFFE9A8)
val LacquerDark = Color(0xFF2A0F06)
val LacquerMid = Color(0xFF5A1E08)
val LacquerWood = Color(0xFF6B2A0C)
val LacquerDeepWood = Color(0xFF431806)
val LacquerFelt = Color(0xFF3B1507)
val LacquerEdge = Color(0xFF8C3503)
val TextPrimary = Color(0xFF3C1A0E)
val TextSecondary = Color(0xFF7A4A2E)
val TextMuted = Color(0xFFA87B5A)
val OnPrimary = Color(0xFFFFF6E2)
val Success = Color(0xFF4E8C4A)
val Danger = Color(0xFFC0392B)
val PaperDeep = Color(0xFFF6E6C8)
val SurfaceSelected = Color(0xFFFFF4D2)

val SplashOverlay = listOf(
    LacquerDark.copy(alpha = 0.94f),
    LacquerMid.copy(alpha = 0.92f),
    LacquerEdge.copy(alpha = 0.90f)
)
val MenuOverlay = listOf(
    BgCream.copy(alpha = 0.10f),
    BgCream.copy(alpha = 0.55f),
    BgCream.copy(alpha = 0.97f)
)
val GameOverlay = listOf(
    LacquerDark.copy(alpha = 0.55f),
    LacquerMid.copy(alpha = 0.42f),
    BgCream.copy(alpha = 0.90f)
)
val WinOverlay = listOf(
    BgCream.copy(alpha = 0.80f),
    LuckyGold.copy(alpha = 0.45f),
    BgCream.copy(alpha = 0.96f)
)
val LoseOverlay = listOf(
    BgCream.copy(alpha = 0.85f),
    SurfaceMuted.copy(alpha = 0.75f),
    BgCream.copy(alpha = 0.97f)
)
val LevelMapOverlay = listOf(BgCream.copy(alpha = 0.88f), BgSand.copy(alpha = 0.97f))
val BonusOverlay = listOf(
    BgCream.copy(alpha = 0.92f),
    LuckyGold.copy(alpha = 0.30f),
    BgCream.copy(alpha = 0.97f)
)
val RewardsOverlay = listOf(BgCream.copy(alpha = 0.90f), BgSand.copy(alpha = 0.96f))
val AwardsOverlay = listOf(BgCream.copy(alpha = 0.93f), SurfaceMuted.copy(alpha = 0.92f))
val StatsOverlay = listOf(BgCream.copy(alpha = 0.94f), SurfaceAlt.copy(alpha = 0.95f))
val SettingsOverlay = listOf(BgCream.copy(alpha = 0.95f), SurfaceMuted.copy(alpha = 0.93f))

val GoldSweep = listOf(LuckyGold, DeepGold, PaleGold, DeepGold, LuckyGold)
val CtaGradient = listOf(AmberOrange, TigerOrange)
val LeverGradient = listOf(LuckyGold, DeepGold)
val WoodGradient = listOf(LacquerWood, LacquerDeepWood)
val PaperGradient = listOf(SurfaceCard, PaperDeep)
val MutedGradient = listOf(SurfaceMuted, SurfaceMuted)
