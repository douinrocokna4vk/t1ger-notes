package com.Telqopu0.Kitdag.domain.model

enum class LineKind(val bonus: Float) {
    ROW(1.0f),
    COLUMN(1.2f),
    DIAGONAL(1.5f)
}

data class WinLine(
    val cells: List<Int>,
    val symbol: Symbol,
    val kind: LineKind,
    val points: Int
)
