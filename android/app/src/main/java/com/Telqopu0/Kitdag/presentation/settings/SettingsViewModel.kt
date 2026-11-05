package com.Telqopu0.Kitdag.presentation.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.Telqopu0.Kitdag.domain.model.AppSettings
import com.Telqopu0.Kitdag.domain.repository.ProgressRepository
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SettingsViewModel(
    private val repository: ProgressRepository
) : ViewModel() {

    private val _state = MutableStateFlow(SettingsUiState())
    val state: StateFlow<SettingsUiState> = _state.asStateFlow()

    private val _events = Channel<Unit>(Channel.BUFFERED)
    val events: Flow<Unit> = _events.receiveAsFlow()

    init {
        refresh()
    }

    fun refresh() {
        viewModelScope.launch {
            _state.update { it.copy(settings = repository.settings()) }
        }
    }

    fun setSound(value: Boolean) {
        update { it.copy(sound = value) }
    }

    fun setVibration(value: Boolean) {
        update { it.copy(vibration = value) }
    }

    fun setFastAnimations(value: Boolean) {
        update { it.copy(fastAnimations = value) }
    }

    fun askReset() {
        _state.update { it.copy(confirmVisible = true) }
    }

    fun cancelReset() {
        _state.update { it.copy(confirmVisible = false) }
    }

    fun confirmReset() {
        viewModelScope.launch {
            repository.reset()
            _state.update {
                it.copy(settings = repository.settings(), confirmVisible = false, resetDone = true)
            }
            _events.send(Unit)
        }
    }

    private fun update(transform: (AppSettings) -> AppSettings) {
        viewModelScope.launch {
            val updated = transform(_state.value.settings)
            repository.saveSettings(updated)
            _state.update { it.copy(settings = updated) }
        }
    }
}
