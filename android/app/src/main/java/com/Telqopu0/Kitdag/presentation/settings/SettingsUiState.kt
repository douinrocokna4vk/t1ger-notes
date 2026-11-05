package com.Telqopu0.Kitdag.presentation.settings

import com.Telqopu0.Kitdag.domain.model.AppSettings

data class SettingsUiState(
    val settings: AppSettings = AppSettings(),
    val confirmVisible: Boolean = false,
    val resetDone: Boolean = false
)
